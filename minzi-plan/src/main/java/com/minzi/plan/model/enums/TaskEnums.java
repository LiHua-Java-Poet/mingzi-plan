package com.minzi.plan.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TaskEnums {

    @Getter
    @AllArgsConstructor
    public enum PlanType {
        XUE_XI(1, "学习"),
        DUA_LIAN(2, "锻炼"),
        XIE_ZUO(3, "写作"),
        YUE_DU(4, "阅读"),
        YIN_SI(5, "影视");
        private final Integer code;
        private final String name;

        public static <K, V> Map<K, V> toMap(Function<PlanType, K> k, Function<PlanType, V> v) {
            return Arrays.stream(PlanType.values()).collect(Collectors.toMap(k, v));
        }
    }
}
