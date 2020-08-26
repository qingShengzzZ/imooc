package cn.szh.seckill.feign;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name="pay")
@RequestMapping("/weixin/pay")
public interface WeixinPayFeign {


    /**
     * 添加积分
     * @param orderId
     * @return
     */
    @GetMapping(value = "/close")
    public Result closePay(Long orderId);





}
