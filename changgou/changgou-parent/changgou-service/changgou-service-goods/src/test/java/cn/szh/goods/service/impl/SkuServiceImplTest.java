package cn.szh.goods.service.impl;


import cn.szh.order.pojo.OrderItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SkuServiceImplTest {
    @Autowired
    private StringRedisTemplate redisTemplate;


    @Test
    public  void  redisTest(){

        String username="szitheima";
        List<Object> values = redisTemplate.boundHashOps("Cart_" + username).values();

//        List values = redisTemplate.opsForHash().values("Cart_szitheima");
        System.out.println(values);
    }

}
