package com.minzi.plan.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TaskLogEnums {

                            
    public static <K, V> Map<K, V> toMap(Function<UserEnums.UserType, K> k, Function<UserEnums.UserType, V> v) {
        return Arrays.stream(UserEnums.UserType.values()).collect(Collectors.toMap(k, v));
    }
}
