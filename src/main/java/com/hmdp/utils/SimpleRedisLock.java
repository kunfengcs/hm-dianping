package com.hmdp.utils;

import cn.hutool.core.lang.UUID;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * SimpleRedisLock 基于lua 脚本
 */
public class SimpleRedisLock implements ILock{

    private String name;

    private StringRedisTemplate stringRedisTemplate;

    public SimpleRedisLock(String name, StringRedisTemplate stringRedisTemplate) {
        this.name = name;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 线程ID前缀，使用UUID生成随机字符串以确保唯一性
     */
    private static final String ID_PREFIX = UUID.randomUUID().toString(true)+ "-";

    /**
     * 解锁Lua脚本，默认RedisScript实例，用于原子性的解锁操作
     */
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;

    //静态块，加载并设置解释Lua脚本
    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("unlock.lua"));//设置Lua脚本路径
        UNLOCK_SCRIPT.setResultType(Long.class);//设置返回结果类型为Long
    }
    private static final String KEY_PREFIX = "lock:";
    @Override
    public boolean tryLock(long timeoutSec) {
        //获取线程标识
        String threadId = ID_PREFIX +Thread.currentThread().getId();
        //获取锁
        Boolean success = stringRedisTemplate.opsForValue()
                .setIfAbsent(KEY_PREFIX + name, threadId + "", timeoutSec, TimeUnit.SECONDS);
        //Boolean 转-> boolean 拆箱，可能出现空指针异常，使用Boolean.TRUE.equals(success);null也为false
        return Boolean.TRUE.equals(success);
    }


    /**
     * 释放锁
     * 通过执行Lua脚本来保证解锁操作的原子性
     */
    @Override
    public void unlock(){
        // 获取当前线程的唯一标识
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        //调用Lua脚本进行解锁操作
        stringRedisTemplate.execute(
                UNLOCK_SCRIPT,
                Collections.singletonList(KEY_PREFIX + name),//参数列表：锁的key
                threadId // 参数： 当前线程的唯一标识
        );
    }
    /**
     * **注释已移除的方法：**
     *
     * 上述方法是一个非原子性的解锁方式，这里为了实现原子性解锁，采用了lua脚本的方式，因此注释掉了这个原始的unlock方法。
     */
    /*@Override
    public void unlock() {
        // 获取线程标示
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        // 获取锁中的标示
        String id = stringRedisTemplate.opsForValue().get(KEY_PREFIX + name);
        // 判断标示是否一致
        if(threadId.equals(id)) {
            // 释放锁
            stringRedisTemplate.delete(KEY_PREFIX + name);
        }
    }*/
}
