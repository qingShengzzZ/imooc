package cn.szh.order.service.impl;

import cn.szh.goods.feign.SkuFeign;
import cn.szh.goods.feign.SpuFeign;
import cn.szh.goods.pojo.Sku;
import cn.szh.goods.pojo.Spu;
import cn.szh.order.pojo.OrderItem;
import cn.szh.order.service.CartService;
import cn.szh.order.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private RedisTemplate redisTemplate;



    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SpuFeign spuFeign;


    /***
     * 加入购物车
     * @param num:购买商品数量
     * @param id：购买ID
     * @param username：购买用户
     * @return
     */
    @Override
    public void add(Integer num, Long id, String username) {
        if(num <= 0){
            redisUtil.hdel("Cart_"+username,id.toString());
            return;
        }
        //查询SKU
        Result<Sku> resultSku = skuFeign.findById(id);
        if(resultSku!=null && resultSku.isFlag()){
            //获取SKU
            Sku sku = resultSku.getData();
            //获取SPU
            Result<Spu> resultSpu = spuFeign.findById(sku.getSpuId());

            //将SKU转换成OrderItem
            OrderItem orderItem = sku2OrderItem(sku,resultSpu.getData(), num);

            /******
             * 购物车数据存入到Redis
             * namespace = Cart_[username]
             * key=id(sku)
             * value=OrderItem
             */
//            redisTemplate.boundHashOps("Cart_"+username).put(id,orderItem);

            redisUtil.hset("Cart_"+username,id.toString(),orderItem);

        }
    }

    /***
     * 查询用户购物车数据
     * @param username
     * @return
     */
    @Override
    public List<OrderItem> list(String username) {
        //查询所有购物车数据
        List<OrderItem> orderItems = redisTemplate.boundHashOps("Cart_"+username).values();

//        Map<Object, Object> hmget = redisUtil.hmget("Cart_" + username);
//        Object o = hmget.get("1148477873175142400");
//        OrderItem orderItem = JSON.parseObject(JSON.toJSONString(o), OrderItem.class);
//        List<OrderItem> orderItems =new ArrayList<>();
//        orderItems.add(orderItem);

//        List<OrderItem> orderItems = (List<OrderItem>) redisUtil.hmget("Cart_" + username);
        return orderItems;
    }

    /***
     * SKU转成OrderItem
     * @param sku
     * @param num
     * @return
     */
    private OrderItem sku2OrderItem(Sku sku,Spu spu,Integer num){
        OrderItem orderItem = new OrderItem();
        orderItem.setSpuId(sku.getSpuId());
        orderItem.setSkuId(sku.getId());
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(num);
        orderItem.setMoney(num*orderItem.getPrice());       //单价*数量
        orderItem.setPayMoney(num*orderItem.getPrice());    //实付金额
        orderItem.setImage(sku.getImage());
        orderItem.setWeight(sku.getWeight()*num);           //重量=单个重量*数量

        //分类ID设置
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        return orderItem;
    }
}
