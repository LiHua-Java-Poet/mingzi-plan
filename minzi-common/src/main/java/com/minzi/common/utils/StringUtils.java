package com.minzi.common.utils;

public class StringUtils {

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

}
