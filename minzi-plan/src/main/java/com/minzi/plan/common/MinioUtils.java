package com.minzi.plan.common;

import com.alibaba.fastjson.JSONObject;
import com.minzi.plan.config.MinioProp;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.annotation.Resource;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Iterator;

@Slf4j
@Component
public class MinioUtils {

    @Resource
    private MinioClient client;
    @Resource
    private MinioProp minioProp;

    /**
     * 创建bucket
     *
     * @param bucketName bucket名称
     */
    @SneakyThrows
    public void createBucket(String bucketName) {
        if (!client.bucketExists(bucketName)) {
            client.makeBucket(bucketName);
        }
    }

    /**
     * 上传文件
     *
     * @param file       文件
     * @param bucketName 存储桶
     * @return
     */
    public JSONObject uploadFile(MultipartFile file, String bucketName) throws Exception {
        JSONObject res = new JSONObject();
        res.put("code", 0);
        // 判断上传文件是否为空
        if (null == file || 0 == file.getSize()) {
            res.put("msg", "上传文件不能为空");
            return res;
        }
        try {
            // 判断存储桶是否存在
            createBucket(bucketName);
            // 文件名
            String originalFilename = file.getOriginalFilename();
            // 新的文件名 = 存储桶名称_时间戳.后缀名
            String fileName = bucketName + "_" + System.currentTimeMillis() + originalFilename.substring(originalFilename.lastIndexOf("."));
            // 开始上传
            client.putObject(bucketName, fileName, file.getInputStream(), file.getContentType());
            res.put("code", 1);
            res.put("msg", minioProp.getEndpoint() + "/" + bucketName + "/" + fileName);
            return res;
        }  catch (Exception e) {
            log.error("上传文件失败：{}", e.getMessage());
        }
        res.put("msg", "上传失败");
        return res;
    }

    public byte[] downloadFile(String file_path){
        try {
            InputStream inputStream = client.getObject("store",file_path);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // 获取最终的字节数组
            byte[] byteArray = outputStream.toByteArray();
            return byteArray;
        }catch (Exception e){

        }
        // 下载对象
        return null;
    }

    public InputStreamResource downloadFiles(String file_path){
        try {
            InputStream inputStream = client.getObject("store",file_path);
            InputStreamResource resource=new InputStreamResource(inputStream);
            return resource;
        }catch (Exception e){

        }
        // 下载对象
        return null;
    }

    public int uploadOneFile(MultipartFile file,String fileName){
        try {
            System.out.println(file.getContentType());
            client.putObject("store", fileName, file.getInputStream(), file.getContentType());
        }catch (Exception e){
            return 0;
        }
        return 1;
    }

    public int uploadChunkFile(InputStream file,String fileName,String file_type){
        try {
            client.putObject("store", fileName, file, file_type);
        }catch (Exception e){
            return 0;
        }
        return 1;
    }

    public String saveImageCover(MultipartFile file){
        try {
            System.out.println("压缩前大小:"+getFileSizeInHumanReadable(file.getSize()));
            // 读取MultipartFile内容到BufferedImage
            BufferedImage originalImage = ImageIO.read(file.getInputStream());

            // 设置压缩质量（例如：0.5表示中等压缩）
            float quality = 0.5f;

            // 获取JPEG的ImageWriter
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = (ImageWriter) writers.next();

            // 设置输出流
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream);
            writer.setOutput(ios);

            // 设置压缩参数
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);

            // 写入压缩后的图片
            writer.write(null, new IIOImage(originalImage, null, null), param);

            // 关闭流和writer
            ios.close();
            writer.dispose();

            // 将ByteArrayOutputStream转换为InputStream，以便上传到MinIO
            InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            // ... 省略MinIO上传代码 ...
            String originalFilename=file.getOriginalFilename();
            String file_path=System.currentTimeMillis()+"_"+ originalFilename.substring(originalFilename.lastIndexOf("."));
            client.putObject("images",file_path,inputStream,file.getContentType());
            return "http://112.74.191.203:9000/images/"+file_path;
        } catch (IOException | InvalidBucketNameException | NoSuchAlgorithmException | InsufficientDataException |
                 InvalidKeyException | NoResponseException | XmlPullParserException | ErrorResponseException |
                 InternalException | InvalidArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String saveImageCover(InputStream inputStream,String file_path,Float quality,String file_type){
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // 读取InputStream到BufferedImage
            BufferedImage image = ImageIO.read(inputStream);

            // 获取JPEG ImageWriter
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            if (writers.hasNext()) {
                ImageWriter writer = writers.next();
                try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
                    writer.setOutput(ios);

                    // 创建一个默认的写入参数
                    ImageWriteParam param = writer.getDefaultWriteParam();

                    // 设置压缩模式和质量
                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    param.setCompressionQuality(quality);

                    // 写入压缩后的图片到ByteArrayOutputStream
                    writer.write(null, new IIOImage(image, null, null), param);
                } finally {
                    writer.dispose();
                }
            }

            // 将ByteArrayOutputStream转换为ByteArrayInputStream
            InputStream compressedInputStream=new ByteArrayInputStream(baos.toByteArray());
            client.putObject("images",file_path,compressedInputStream,file_type);
            return "http://112.74.191.203:9000/images/"+file_path;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private BufferedImage compressImage(BufferedImage image) {
        // Adjust JPEG compression quality to reduce file size
        // You can adjust the quality factor according to your requirements
        // Lower quality means smaller file size but reduced image quality
        double quality = 0.5; // Example quality factor
        BufferedImage compressedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Write the compressed image to the output stream
        try {
            ImageIO.write(image, "jpg", outputStream);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            compressedImage = ImageIO.read(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return compressedImage;
    }

    /**
     * 字节数转换为大小
     * @param size
     * @return
     */
    public static String getFileSizeInHumanReadable(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[] { "bytes", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups))  + units[digitGroups];
    }
}
