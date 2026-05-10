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

        public static <K, V> Map<K, V> toMap(Function<SysMenuEnums.SysMenuStatus, K> k, Function<SysMenuEnums.SysMenuStatus, V> v) {
            return Arrays.stream(SysMenuEnums.SysMenuStatus.values()).collect(Collectors.toMap(k, v));
        }
    }

    @Getter
    @AllArgsConstructor
    public enum MenuType {
        PU_TON(1, "普通菜单"),
        XI_TON(2, "系统菜单");
        private final Integer code;
        private final String name;

        public static <K, V> Map<K, V> toMap(Function<SysMenuEnums.MenuType, K> k, Function<SysMenuEnums.MenuType, V> v) {
            return Arrays.stream(SysMenuEnums.MenuType.values()).collect(Collectors.toMap(k, v));
        }
    }
}