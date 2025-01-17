package com.minzi.common.utils;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface PropertyFunc<T, R> extends Function<T, R>, Serializable {
}
