package com.pinyougou.cart.service;


import com.pinyougou.pojogroup.Cart;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface CartService {


    /**
     * 添加商品到购物车列表
     * @param cartList
     * @param itemId
     * @param num
     * @return
     */
   List<Cart> addGoodsToCartList(List<Cart> cartList,Long itemId,Integer num);


    /**
     * 从redis中提取购物车列表
     * @param username
     * @return
     */
   List<Cart> findCartListFromRedis(String username);

    /**
     * 将购物车列表存入redis
     * @param username
     * @param cartList
     */
   void saveCartListToRedis(String username,List<Cart> cartList);

    /**
     * 合并购物车
     * @param carList1
     * @param carList2
     * @return
     */
   List<Cart> mergeCarList(List<Cart> carList1 ,List<Cart> carList2);
}
