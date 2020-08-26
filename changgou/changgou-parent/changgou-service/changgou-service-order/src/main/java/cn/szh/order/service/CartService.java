package cn.szh.order.service;

import cn.szh.order.pojo.OrderItem;

import java.util.List;

public interface CartService {
    void add(Integer num, Long id, String username);
    /***
     * 查询用户的购物车数据
     * @param username
     * @return
     */
    List<OrderItem> list(String username);
}
