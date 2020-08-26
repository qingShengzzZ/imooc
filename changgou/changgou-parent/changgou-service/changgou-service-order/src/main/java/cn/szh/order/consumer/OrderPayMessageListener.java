package cn.szh.order.consumer;


import cn.szh.order.service.OrderService;
import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = {"${mq.pay.queue.order}"})
public class OrderPayMessageListener {
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitHandler
    public void consumeMessage(String msg){
        Map<String,String> result =JSON.parseObject(msg, Map.class);

        String return_code = result.get("return_code");
        String retult_code = result.get("retult_code");
        if (return_code.equalsIgnoreCase("success")) {
            String outTradeNo = result.get("out_trade_no");
            if (retult_code.equalsIgnoreCase("success")) {
                if (outTradeNo != null) {
                    orderService.updateStatus(outTradeNo,result.get("transaction_id"));
                }
            }else {
                //订单删除
                orderService.deleteOrder(outTradeNo);
        }
        }
    }

}
