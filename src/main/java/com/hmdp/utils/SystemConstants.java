package com.hmdp.utils;

public class SystemConstants {
//    public static final String IMAGE_UPLOAD_DIR = "D:\\heima_dianping\\nginx-1.18.0\\html\\hmdp\\imgs\\";
    //返回的是执行Java程序时用户的工作目录（通常是启动JVM的那个目录）
    public static final String IMAGE_UPLOAD_DIR = System.getProperty("user.dir")+
        "\\hm-dianping_front\\nginx-1.18.0\\html\\hmdp\\imgs\\";
    public static final String USER_NICK_NAME_PREFIX = "user_";
    public static final int DEFAULT_PAGE_SIZE = 5;
    public static final int MAX_PAGE_SIZE = 10;
    //过期时间
    public static final long EXPIRATION_TIME = 604800;
}
