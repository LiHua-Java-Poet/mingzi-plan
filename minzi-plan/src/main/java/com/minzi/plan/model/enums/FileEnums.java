package com.minzi.plan.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FileEnums {

    @Getter
    @AllArgsConstructor
    public enum MenuType {
        WEN_DANG(1, "文档"),
        WEN_JIAN_JIA(2, "文件夹");
        private final Integer code;
        private final String name;

        public static <K, V> Map<K, V> toMap(Function<PlanEnums.PlanType, K> k, Function<PlanEnums.PlanType, V> v) {
            return Arrays.stream(PlanEnums.PlanType.values()).collect(Collectors.toMap(k, v));
        }
    }
}
