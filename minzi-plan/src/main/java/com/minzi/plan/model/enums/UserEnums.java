package com.minzi.plan.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserEnums {

    @Getter
    @AllArgsConstructor
    public enum UserType {
        YON_HU(1, "用户"),
        GUAN_LI(2, "管理");
        private final Integer code;
        private final String name;
    }
    public static <K, V> Map<K, V> toMap(Function<UserEnums.UserType, K> k, Function<UserEnums.UserType, V> v) {
        return Arrays.stream(UserEnums.UserType.values()).collect(Collectors.toMap(k, v));
    }
}
