package com.example.demo;


import com.example.demo.config.RedisCacheAutoConfiguration;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;


@RunWith(SpringRunner.class)
@SpringBootTest

public class RedisTests {
    private static Logger log = LoggerFactory.getLogger(RedisTests.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<String,Serializable> redisTemplate;


    @Before
    public void initData(){

    }
    @Test
    public void contextLoads() throws Exception{

        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        IntStream.range(0,1000).forEach(
                i ->
                        executorService.execute(() -> stringRedisTemplate.opsForValue().increment("kk", 1))
        );
        stringRedisTemplate.opsForValue().set("k1", "v1");
        final String k1 = stringRedisTemplate.opsForValue().get("k1");

        log.info("[字符集结果] - [{}]", k1);

        String key = "bcfou:user:1";

        redisTemplate.opsForValue().set(key, (Serializable) new User(1L, "u1", "pa"));
        // TODO 对应 String（字符串）
        final User user = (User) redisTemplate.opsForValue().get(key);
        log.info("[对象缓存结果] - [{}]", user);
    }
}
