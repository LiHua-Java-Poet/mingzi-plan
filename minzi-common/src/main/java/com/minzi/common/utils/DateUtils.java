package com.minzi.common.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static Integer currentDateTime(){
        long currentTimeMillis = System.currentTimeMillis();
        return (int) (currentTimeMillis / 1000);
    }

    public static String currentDate(){
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 格式化日期为字符串
        return currentDate.format(formatter);
    }
}
