package cn.szh.pay.controller;

import cn.szh.pay.service.WeixinPayService;
import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/weixin/pay")
@CrossOrigin
public class WeixinPayController {
    @Value("${mq.pay.exchange.order}")
    private String exchange;
    @Value("${mq.pay.queue.order}")
    private String queue;
    @Value("${mq.pay.routing.key}")
    private String routing;
    @Autowired
    private Environment env;
    @Autowired
    private WeixinPayService weixinPayService;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /***
     * 支付回调
     * @param request
     * @return
     */
    @PostMapping(value = "/notify/url")
    public String notifyUrl(HttpServletRequest request){
        System.out.println("=========================回调成功==================================");
        InputStream inStream;
        try {
            //读取支付回调数据
            inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            // 将支付回调数据转换成xml字符串
            String result = new String(outSteam.toByteArray(), "utf-8");
            System.out.println(result);
            //将xml字符串转换成Map结构
            Map<String, String> map = WXPayUtil.xmlToMap(result);
//4.发送消息给Rabbitmq  .........
            String data = JSON.toJSONString(map);
            //动态的从attach参数中获取数据
            String attach = map.get("attach");
            // {routingkey=queue.seckillorder, exchange=exchange.seckillorder, queue=queue.seckillorder, username=szitheima}
            Map<String,String> attachMap = JSON.parseObject(attach, Map.class);// 已经有
            System.out.println(attachMap);
            //发送消息
            //rabbitTemplate.convertAndSend(env.getProperty("mq.pay.exchange.order"),env.getProperty("mq.pay.routing.key"),data);
            rabbitTemplate.convertAndSend(attachMap.get("exchange"),attachMap.get("routingkey"),data);

            //响应数据设置
            Map respMap = new HashMap();
            respMap.put("return_code","SUCCESS");
            respMap.put("return_msg","OK");
            return WXPayUtil.mapToXml(respMap);
        } catch (Exception e) {
            e.printStackTrace();
            //记录错误日志
        }
        return null;
    }

    /***
     * 创建二维码
     * @return
     */
    @RequestMapping(value = "/create/native")
    public Result createNative(@RequestParam Map<String,String> parameter){
        Map<String,String> resultMap = weixinPayService.createNative(parameter);
        return new Result(true, StatusCode.OK,"创建二维码预付订单成功！",resultMap);
    }
    /***
     * 查询支付状态
     * @param outtradeno
     * @return
     */
    @GetMapping(value = "/status/query")
    public Result queryStatus(String outtradeno){
        Map<String,String> resultMap = weixinPayService.queryPayStatus(outtradeno);
        rabbitTemplate.convertAndSend(env.getProperty("mq.pay.exchange.seckillorder"),env.getProperty("mq.pay.routing.seckillorder"),"attach");

        return new Result(true,StatusCode.OK,"查询状态成功！",resultMap);
    }
    @RequestMapping("/test")
    public String test(){
        //动态的从attach参数中获取数据
        Map<String,String> attach = new HashMap<>();
        attach.put("username","zhangsan");
        attach.put("queue","queue.seckillorder");//队列名称
        attach.put("routingKey","queue.seckillorder");//路由key
        attach.put("exchange","exchange.seckillorder");
        // {routingkey=queue.seckillorder, exchange=exchange.seckillorder, queue=queue.seckillorder, username=szitheima}
//        rabbitTemplate.convertAndSend(env.getProperty("mq.pay.exchange.order"),env.getProperty("mq.pay.routing.key"),attach);
        rabbitTemplate.convertAndSend(attach.get("exchange"),attach.get("routingKey"),attach);

        return null;
    }
    @RequestMapping("/close")
    public Result closepay(Long orderId) throws Exception {
        Map<String, String> resultMap = weixinPayService.closePay(orderId);
        return new Result(true,StatusCode.OK,"查询状态成功！",resultMap);

    }

}
