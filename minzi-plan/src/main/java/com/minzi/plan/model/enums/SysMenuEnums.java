package com.minzi.plan.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SysMenuEnums {

    @Getter
    @AllArgsConstructor
    public enum SysMenuStatus {
        ZENG_CHANG(1, "正常"),
        TING_YON(2, "停用");
        private final Integer code;
        private final String name;

        public static <K, V> Map<K, V> toMap(Function<TaskEnums.PlanType, K> k, Function<TaskEnums.PlanType, V> v) {
            return Arrays.stream(TaskEnums.PlanType.values()).collect(Collectors.toMap(k, v));
        }
    }
}
