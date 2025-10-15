package com.minzi.common.core.loadClass;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

public class JarDecryptor {

    private static final int AES_KEY_BITS = 128;
    private static final int GCM_IV_LENGTH = 12; // bytes
    private static final int GCM_TAG_LENGTH = 128; // bits
    private static final int PBKDF2_ITER = 100_000;

    /**
     * 解密加密的 JAR 文件，返回临时解密后的 JAR 文件
     *
     * @param encJarPath .enc 文件
     * @param password   密码
     * @return 临时解密后的 JAR 文件
     * @throws Exception
     */
    public static File decryptJarToTempFile(String encJarPath, char[] password) throws Exception {
        File encJar = new File(encJarPath);
        //这个是计算机的临时目录
        File tempJar = File.createTempFile("decrypted-", ".jar");
        tempJar.deleteOnExit();

        try (JarInputStream jis = new JarInputStream(Files.newInputStream(encJar.toPath()));
             JarOutputStream jos = new JarOutputStream(Files.newOutputStream(tempJar.toPath()))) {

            // 读取盐
            JarEntry entry = jis.getNextJarEntry();
            if (entry == null || !entry.getName().equals("__meta/salt")) {
                throw new RuntimeException("找不到盐信息");
            }

            byte[] salt = readAllBytes(jis);
            SecretKey key = deriveKey(password, salt);

            // 遍历剩下的 entries 即文件,一次进行解密
            while ((entry = jis.getNextJarEntry()) != null) {
                JarEntry outEntry = new JarEntry(entry.getName());
                jos.putNextEntry(outEntry);

                byte[] data = readAllBytes(jis);

                if (entry.getName().endsWith(".class")) {
                    // class 文件解密
                    byte[] iv = new byte[GCM_IV_LENGTH];
                    System.arraycopy(data, 0, iv, 0, GCM_IV_LENGTH);
                    byte[] cipherText = new byte[data.length - GCM_IV_LENGTH];
                    System.arraycopy(data, GCM_IV_LENGTH, cipherText, 0, cipherText.length);

                    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                    GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
                    cipher.init(Cipher.DECRYPT_MODE, key, spec);
                    byte[] plain = cipher.doFinal(cipherText);

                    jos.write(plain);
                } else {
                    // 资源文件原样写
                    jos.write(data);
                }

                jos.closeEntry();
            }
        }

        return tempJar;
    }

    /**
     * 解密加密的 JAR 文件，返回临时解密后的 JAR 字节流
     * @param encJarPath .enc 文件
     * @param password 密码
     * @return 临时解密后的 JAR 字节流
     * @throws Exception
     */
    public static byte[] decryptJarToBytes(String encJarPath, char[] password) throws Exception {
        try (JarInputStream jis = new JarInputStream(Files.newInputStream(Paths.get(encJarPath)))) {

            // 读取盐
            JarEntry entry = jis.getNextJarEntry();
            if (entry == null || !entry.getName().equals("__meta/salt")) {
                throw new RuntimeException("找不到盐信息");
            }
            byte[] salt = readAllBytes(jis);
            SecretKey key = deriveKey(password, salt);

            // 遍历剩下的 entries,把解密后的 class 和资源写入内存 jar
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (java.util.jar.JarOutputStream jos = new java.util.jar.JarOutputStream(baos)) {

                while ((entry = jis.getNextJarEntry()) != null) {
                    JarEntry outEntry = new JarEntry(entry.getName());
                    jos.putNextEntry(outEntry);

                    byte[] data = readAllBytes(jis);

                    if (entry.getName().endsWith(".class")) {
                        // class 文件解密
                        byte[] iv = new byte[GCM_IV_LENGTH];
                        System.arraycopy(data, 0, iv, 0, GCM_IV_LENGTH);
                        byte[] cipherText = new byte[data.length - GCM_IV_LENGTH];
                        System.arraycopy(data, GCM_IV_LENGTH, cipherText, 0, cipherText.length);

                        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
                        cipher.init(Cipher.DECRYPT_MODE, key, spec);
                        byte[] plain = cipher.doFinal(cipherText);

                        jos.write(plain);
                    } else {
                        // 资源文件原样写
                        jos.write(data);
                    }

                    jos.closeEntry();
                }
            }

            return baos.toByteArray();
        }
    }

    private static SecretKey deriveKey(char[] password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password, salt, PBKDF2_ITER, AES_KEY_BITS);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = skf.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }

    // Java 8 兼容 readAllBytes
    private static byte[] readAllBytes(JarInputStream jis) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int read;
        while ((read = jis.read(buffer)) != -1) {
            baos.write(buffer, 0, read);
        }
        return baos.toByteArray();
    }
}
