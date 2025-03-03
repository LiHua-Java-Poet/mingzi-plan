package com.minzi.common.tools;

import com.minzi.common.utils.PropertyFunc;

import java.util.List;

public interface OneToManyAct{

    default <T> void oneToMany(T item, PropertyFunc<T, ?> mapFieldFunc) {
        if (item == null ) return;
    }

    default <T> void oneToMany(List<T> itemList, PropertyFunc<T, ?> mapFieldFunc) {
        if (itemList == null || itemList.isEmpty()) return;

    }
}
