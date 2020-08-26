package cn.szh.pay.service;

import java.util.Map;

public interface WeixinPayService {
    /*****
     * 创建二维码
     * @param parameter
     * @return
     */
    public Map createNative(Map<String,String> parameter);
    /***
     * 查询订单状态
     * @param out_trade_no : 客户端自定义订单编号
     * @return
     */
    public Map queryPayStatus(String out_trade_no);
    /***
     * 关闭支付
     * @param orderId
     * @return
     */
    Map<String,String> closePay(Long orderId) throws Exception;
}
