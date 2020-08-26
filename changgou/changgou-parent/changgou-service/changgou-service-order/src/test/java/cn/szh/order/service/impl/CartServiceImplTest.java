package cn.szh.order.service.impl;

import cn.szh.OrderApplication;
import cn.szh.order.pojo.OrderItem;
import com.netflix.discovery.converters.Auto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApplication.class)
@WebAppConfiguration
public class CartServiceImplTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void redisTest(){
//        List<OrderItem> list=new ArrayList<>();
//
//        String username = "szitheima";
//        List<OrderItem> orderItems = redisTemplate.boundHashOps("Cart_"+username).values();
////        List list1 = redisTemplate.opsForHash().multiGet("Cart_" + username, list);
//        System.out.println(orderItems);1131814843356155904
        Set keys = redisTemplate.boundHashOps("SeckillGoods_" + 2020081714).keys();
        System.out.println(keys);
    }



}
