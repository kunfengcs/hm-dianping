package com.hmdp.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 全局id工具类
 *
 * @author 王坤峰
 */
@Component
public class RedisIdWorker {

    /**
     * 开始时间戳
     */
    private static final long BEGIN_TIMESTAMP = 1706267572722L;

    /**
     * 序列号的位数
     */
    private static final int COUNT_BITS  = 32;

    private StringRedisTemplate stringRedisTemplate;

    public RedisIdWorker(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public long nextId(String keyPrefix){
        //1,生成时间戳
        LocalDateTime now = LocalDateTime.now();
        /*        toEpochSecond是LocalDateTime类中的方法，用于将日期时间转换为从1970年1月1日零时零分零秒（UTC）开始的秒数。
        ZoneOffset.UTC表示协调世界时（UTC）的时区偏移，即零时区*/
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;

        //2,生成序列号
        //2.1 获取当前日期，精确到天
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        //2.2自增长
        long count = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);
        //3，拼接并返回
        return timestamp << COUNT_BITS | count;
    }
}
