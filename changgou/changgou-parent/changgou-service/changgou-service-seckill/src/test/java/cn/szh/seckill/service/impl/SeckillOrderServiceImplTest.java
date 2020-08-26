package cn.szh.seckill.service.impl;


import cn.szh.seckill.pojo.SeckillStatus;
import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayUtil;
import entity.SystemConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SeckillOrderServiceImplTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisOperations redisTemplate;

    @Test
    public void redisTest() throws Exception {
//        //排队信息封装
//        String username="itheima";
//        Long id=1131814846480912384L;
//        String time="2020081720";
////        SeckillStatus seckillStatus = new SeckillStatus(username, new Date(),1, id,time);
//        Object o = redisTemplate.boundListOps(SystemConstants.SEC_KILL_CHAOMAI_LIST_KEY_PREFIX + id).rightPop();
//        System.out.println(o);
        //将秒杀抢单信息存入到Redis中,这里采用List方式存储,List本身是一个队列
        //进入排队中
//        redisTemplate.boundListOps(SystemConstants.SEC_KILL_USER_QUEUE_KEY).leftPush(seckillStatus);
//
//        //进入排队标识
//        redisTemplate.boundHashOps(SystemConstants.SEC_KILL_USER_STATUS_KEY).put(username,seckillStatus);
        String result ="<xml><appid><![CDATA[wx8397f8696b538317]]></appid><attach><![CDATA[{\"routingkey\":\"queue.seckillorder\",\"exchange\":\"exchange.seckillorder\",\"queue\":\"queue.seckillorder\",\"username\":\"szitheima\"}]]></attach><bank_type><![CDATA[OTHERS]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[N]]></is_subscribe><mch_id><![CDATA[1473426802]]></mch_id><nonce_str><![CDATA[8808a268571f45a8a5283a88dce92536]]></nonce_str><openid><![CDATA[oNpSGwVzvfwjybsHXOOWzymOcZoM]]></openid><out_trade_no><![CDATA[1131814849576308736]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[41526C41C86A0EDE34712846E38224CE]]></sign><time_end><![CDATA[20200818180707]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[NATIVE]]></trade_type><transaction_id><![CDATA[4200000714202008189933685067]]></transaction_id></xml>";
        Map<String, String> map = WXPayUtil.xmlToMap(result);
//4.发送消息给Rabbitmq  .........
        String data = JSON.toJSONString(map);
        rabbitTemplate.convertAndSend("exchange.seckillorder","queue.seckillorder",data);
    }
}
