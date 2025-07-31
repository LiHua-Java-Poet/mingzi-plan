package com.minzi.common.core.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AnnexFileEnum {

    @Getter
    @AllArgsConstructor
    public enum FileType {
        // 文本和文档类
        TXT(1, "txt"),
        MD(2, "md"),
        RTF(3, "rtf"),
        DOC(4, "doc"),
        DOCX(5, "docx"),
        PDF(6, "pdf"),
        XLS(7, "xls"),
        XLSX(8, "xlsx"),
        PPT(9, "ppt"),
        PPTX(10, "pptx"),
        CSV(11, "csv"),

        // 代码和脚本类
        JS(12, "js"),
        TS(13, "ts"),
        JAVA(14, "java"),
        PY(15, "py"),
        C(16, "c"),
        CPP(17, "cpp"),
        H(18, "h,hpp"),
        CS(19, "cs"),
        PHP(20, "php"),
        HTML(21, "html,htm"),
        CSS(22, "css"),
        JSON(23, "json"),
        XML(24, "xml"),
        SH(25, "sh"),
        BAT(26, "bat"),

        // 图片类
        JPG(27, "jpg,jpeg"),
        PNG(28, "png"),
        GIF(29, "gif"),
        BMP(30, "bmp"),
        SVG(31, "svg"),
        WEBP(32, "webp"),
        TIFF(33, "tif,tiff"),

        // 音频类
        MP3(34, "mp3"),
        WAV(35, "wav"),
        OGG(36, "ogg"),
        FLAC(37, "flac"),
        AAC(38, "aac"),

        // 视频类
        MP4(39, "mp4"),
        AVI(40, "avi"),
        MKV(41, "mkv"),
        MOV(42, "mov"),
        WMV(43, "wmv"),
        FLV(44, "flv"),

        // 压缩与归档类
        ZIP(45, "zip"),
        RAR(46, "rar"),
        SEVENZ(47, "7z"),
        TAR(48, "tar"),
        GZ(49, "gz"),
        BZ2(50, "bz2"),

        // 可执行文件
        EXE(51, "exe"),
        DLL(52, "dll"),
        APK(53, "apk"),
        APP(54, "app"),

        // 系统和配置文件
        INI(55, "ini"),
        CONF(56, "conf"),
        LOG(57, "log"),
        ENV(58, "env");

        private final Integer code;
        private final String name;  // 这里存的是后缀，比如 "env"

        public static <K, V> Map<K, V> toMap(Function<FileType, K> k, Function<FileType, V> v) {
            return Arrays.stream(FileType.values()).collect(Collectors.toMap(k, v));
        }
    }

}
