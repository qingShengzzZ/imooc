package cn.szh.pay.service.impl;

import cn.szh.pay.service.WeixinPayService;
import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayUtil;
import entity.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class WeixinPayServiceImpl implements WeixinPayService {
    @Value("${weixin.appid}")
    private String appid;

    @Value("${weixin.partner}")
    private String partner;

    @Value("${weixin.partnerkey}")
    private String partnerkey;

    @Value("${weixin.notifyurl}")
    private String notifyurl;


    /**
     * @param parameter
     * @return
     */
    @Override
    public Map createNative(Map<String,String> parameter) {
        try {
            Map paramMap = new HashMap();
            paramMap.put("appid", appid);                              //应用ID
            paramMap.put("mch_id", partner);                           //商户ID号
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());   //随机数
            paramMap.put("body", "畅购");                            	//订单描述
            paramMap.put("out_trade_no",parameter.get("outtradeno"));                 //商户订单号
            paramMap.put("total_fee", parameter.get("money"));                      //交易金额
            paramMap.put("spbill_create_ip", "127.0.0.1");           //终端IP
            paramMap.put("notify_url", notifyurl);                    //回调地址
            paramMap.put("trade_type", "NATIVE");

            Map<String,String> attach = new HashMap<>();
            attach.put("username",parameter.get("username"));
            attach.put("queue",parameter.get("queue"));//队列名称
            attach.put("routingkey",parameter.get("routingkey"));//路由key
            attach.put("exchange",parameter.get("exchange"));

            //追加一个附件参数  队列名 路由key  交换机  用户名.....
            paramMap.put("attach", JSON.toJSONString(attach));

            //设置签名(不做,转换的时候自动添加签名)

            System.out.println("参数信息,看看是有attach的数据"+attach);//交易类型

            String paramXML = WXPayUtil.generateSignedXml(paramMap, partnerkey);

            ///3、执行请求
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setHttps(true);
            httpClient.setXmlParam(paramXML);
            httpClient.post();
            //4、获取参数
            String content = httpClient.getContent();
            Map<String, String> stringMap = WXPayUtil.xmlToMap(content);
            System.out.println("stringMap:"+stringMap);

            //5、获取部分页面所需参数
            Map<String,String> dataMap = new HashMap<String,String>();
            dataMap.put("code_url",stringMap.get("code_url"));
            dataMap.put("out_trade_no",parameter.get("outtradeno"));
            dataMap.put("total_fee",parameter.get("money"));

            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 查询订单状态
     * @param out_trade_no : 客户端自定义订单编号
     * @return
     */
    @Override
    public Map queryPayStatus(String out_trade_no) {
        try {
            //1.封装参数
            Map param = new HashMap();
            param.put("appid",appid);                            //应用ID
            param.put("mch_id",partner);                         //商户号
            param.put("out_trade_no",out_trade_no);              //商户订单编号
            param.put("nonce_str",WXPayUtil.generateNonceStr()); //随机字符

            //2、将参数转成xml字符，并携带签名
            String paramXml = WXPayUtil.generateSignedXml(param,partnerkey);

            //3、发送请求
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setHttps(true);
            httpClient.setXmlParam(paramXml);
            httpClient.post();

            //4、获取返回值，并将返回值转成Map
            String content = httpClient.getContent();
            return WXPayUtil.xmlToMap(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 关闭微信支付
     * @param orderId
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, String> closePay(Long orderId) throws Exception {
        //参数设置
        Map<String,String> paramMap = new HashMap<String,String>();
        paramMap.put("appid",appid); //应用ID
        paramMap.put("mch_id",partner);    //商户编号
        paramMap.put("nonce_str",WXPayUtil.generateNonceStr());//随机字符
        paramMap.put("out_trade_no",String.valueOf(orderId));   //商家的唯一编号

        //将Map数据转成XML字符
        String xmlParam = WXPayUtil.generateSignedXml(paramMap,partnerkey);

        //确定url
        String url = "https://api.mch.weixin.qq.com/pay/closeorder";

        //发送请求
        HttpClient httpClient = new HttpClient(url);
        //https
        httpClient.setHttps(true);
        //提交参数
        httpClient.setXmlParam(xmlParam);

        //提交
        httpClient.post();

        //获取返回数据
        String content = httpClient.getContent();

        //将返回数据解析成Map
        return  WXPayUtil.xmlToMap(content);
    }
}
