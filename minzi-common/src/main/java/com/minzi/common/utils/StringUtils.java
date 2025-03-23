package com.minzi.common.utils;

public class StringUtils {

    /**
     * 下划线转小驼峰
     * @param str 字符串
     * @return 返回的内容
     */
    public static String underscoreToCamelCase(String str) {
        // 处理空字符串的情况
        if (str == null || str.isEmpty()) {
            return str;
        }

        // 以 _ 为分隔符拆分字符串
        String[] words = str.split("_");

        // 小驼峰规则：第一个单词全部小写，后续单词首字母大写
        StringBuilder camelCaseString = new StringBuilder();

        // 遍历单词数组
        for (int i = 0; i < words.length; i++) {
            if (i == 0) {
                // 第一个单词首字母小写
                camelCaseString.append(words[i].toLowerCase());
            } else {
                // 后续单词首字母大写，其余字母小写
                camelCaseString.append(capitalizeFirstLetter(words[i]));
            }
        }

        return camelCaseString.toString();
    }

    private static String capitalizeFirstLetter(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }
        // 将单词的首字母大写
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

    /**
     * 获取文件名的后缀
     *
     * @param fileName 文件名（例如：out.png）
     * @return 文件后缀（例如：png），如果没有后缀则返回空字符串
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return ""; // 如果文件名为空，返回空字符串
        }

        // 查找最后一个点的位置
        int lastDotIndex = fileName.lastIndexOf('.');

        // 如果没有点或者点是最后一个字符，返回空字符串
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return "";
        }

        // 截取点后面的部分作为后缀
        return fileName.substring(lastDotIndex + 1);
    }

}
