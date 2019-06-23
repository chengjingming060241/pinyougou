package com.pinyougou.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojogroup.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {

    //1.根据skuid查询商品明细sku对象
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        if (item==null){
            throw new RuntimeException("商品不存在");
        }
        if (!item.getStatus().equals("1")){
            throw new RuntimeException("商品状态不合法");
        }

    //2.根据sku对象查询商家id
        String sellerId = item.getSellerId(); //商家id

    //3.根据商家id在购物车类表中查询购物车对象
        Cart cart = searchCartBySellerId(cartList, sellerId);

        if (cart==null){  //4.如果购物车列表中不存在该商家的购物车

            //4.1 创建一个新的购物车对象
            cart = new Cart();
            cart.setSellerId(sellerId); //商家ID
            cart.setSellerName(item.getSeller()); //商家名称

            List<TbOrderItem> orderItemList = new ArrayList<>(); //创建购物车明细列表
            TbOrderItem orderItem = createOrderItem(item, num);  //创建新的购物车明细对象
            orderItemList.add(orderItem);

            cart.setOrderItems(orderItemList);

            //4.2将新的购物车对象添加到购物车列表中
            cartList.add(cart);

        }else { //5.如果购物车列表中存在该商家的购物车
            //判断该商品是否在该购物车明细列表中存在
            TbOrderItem orderItem = searchOrderItemByItemId(cart.getOrderItems(), itemId);
            if (orderItem==null){
                //5.1如果不在，创建新的购物车明细对象，并添加到该购物车的没明细列表中
                orderItem = createOrderItem(item,num);
                cart.getOrderItems().add(orderItem);

            }else {
                //5.2如果存在。在原有的数量上添加数量，并更新金额
                orderItem.setNum(orderItem.getNum()+num);
                orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue()*orderItem.getNum()));

                //当明细的数量小于等于0，移除当前明细
                if (orderItem.getNum()<=0){
                    cart.getOrderItems().remove(orderItem);
                }
                //当购物车的明细数量为0，在购物车列表中移除此购物车
                if (cart.getOrderItems().size()==0){
                    cartList.remove(cart);
                }
            }
        }

        return cartList;
    }

    @Autowired
    private RedisTemplate redisTemplate;

    //读取购物车
    @Override
    public List<Cart> findCartListFromRedis(String username) {
        System.out.println("从redis提取购物车"+username);
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(username);
        if (cartList==null){
            cartList = new ArrayList<Cart>();
        }
        return cartList;
    }

    //购物车存储到缓存
    @Override
    public void saveCartListToRedis(String username, List<Cart> cartList) {
        System.out.println("向redis存取购物车"+username);
        redisTemplate.boundHashOps("cartList").put(username,cartList);
    }

    //合并购物车
    @Override
    public List<Cart> mergeCarList(List<Cart> carList1, List<Cart> carList2) {

        for (Cart cart : carList2) {
            for (TbOrderItem orderItem : cart.getOrderItems()) {
                carList1 = addGoodsToCartList(carList1,orderItem.getItemId(),orderItem.getNum());
            }
        }

        return carList1;
    }

    //根据商家id在购物车类表中查询购物车对象
    private Cart searchCartBySellerId(List<Cart> cartList,String sellerId){
        for (Cart cart : cartList) {
            if (cart.getSellerId().equals(sellerId)){
                return cart;
            }
        }
        return null;
    }

    //根据skuID在购物车明细列表中查询购物车明细对象
    private TbOrderItem searchOrderItemByItemId(List<TbOrderItem> orderItemList,Long itemId){
        for (TbOrderItem orderItem:orderItemList) {
            if (orderItem.getItemId().longValue()==itemId.longValue()){
                return orderItem;
            }
        }
        return null;
    }


    //创建购物车明细对象 创建订单明细
    private TbOrderItem createOrderItem(TbItem item,Integer num){
        if(num<=0){
            throw new RuntimeException("数量非法");
        }

        TbOrderItem orderItem = new TbOrderItem();
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setItemId(item.getId());
        orderItem.setNum(num);
        orderItem.setPicPath(item.getImage());
        orderItem.setPrice(item.getPrice());
        orderItem.setSellerId(item.getSellerId());
        orderItem.setTitle(item.getTitle());
        orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue()*num));

        return orderItem;
    }



}
