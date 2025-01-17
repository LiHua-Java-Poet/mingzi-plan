package com.minzi.common.tools;

import com.minzi.common.utils.PropertyFunc;

import java.util.List;

public interface BelongsToAct {


    default <T> void belongsTo(T item, PropertyFunc<T, ?> mapFieldFunc){

    }

    default <T> void belongsTo(List<T> item, PropertyFunc<T, ?> mapFieldFunc){

    }
}
