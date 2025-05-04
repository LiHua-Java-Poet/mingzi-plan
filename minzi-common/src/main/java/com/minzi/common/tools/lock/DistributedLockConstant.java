package com.minzi.common.tools.lock;

public class DistributedLockConstant {

    /**
     * 常量的空KEY
     */
    public static final String NONE_KEY = "";

    /**
     * 锁的超时时间，毫秒
     * 即超时续期
     */
    public static final int DEFAULT_EXPIRE_TIME = 15000;

    /**
     * 加锁等待时间，毫秒
     * 默认情况下不设置等待时间
     */
    public static final int DEFAULT_WAIT_TIME=15000;
}
