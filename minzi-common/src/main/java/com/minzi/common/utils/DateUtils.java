package com.minzi.common.utils;

public class DateUtils {

    public static Integer currentDateTime(){
        long currentTimeMillis = System.currentTimeMillis();
        return (int) (currentTimeMillis / 1000);
    }
}
