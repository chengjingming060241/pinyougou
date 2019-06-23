package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pay.service.WeiXinPayService;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.seckill.service.SeckillOrderService;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PayController {

    @Reference(timeout = 10000)
    private WeiXinPayService weiXinPayService;

    @Reference
    private SeckillOrderService  seckillOrderService;


    @RequestMapping("/createNative")
    public Map createNative(){
        //获取当前登录用户
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        //从缓存中提取订单
        TbSeckillOrder seckillOrder = seckillOrderService.searchOrderFromRedisByUserId(name);
        //调用微信支付接口
        if (null!=seckillOrder){
            return weiXinPayService.createNative(seckillOrder.getId()+"",(long)(seckillOrder.getMoney().doubleValue()*100)+"");
        }else {
            return new HashMap();
        }
    }


    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no){
        Result result = null;
        int x = 0;
        //获取当前登录用户
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        while (true){
            Map<String,String> map = weiXinPayService.queryPayStatus(out_trade_no); //调用查询
            if (map==null){
                result = new Result(false,"支付发送错误");
                break;
            }
            if(map.get("trade_state").equals("SUCCESS")) {
                result = new Result(true,"支付成功");
                //保存订单
                seckillOrderService.saveOrderFromRedisToDb(userName,Long.valueOf(out_trade_no),map.get("transaction_id"));
                break;
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            x++;
            if (x>=100){
                result = new Result(false,"支付超时");
                //关闭支付订单
                Map<String,String > payResult = weiXinPayService.closePay(out_trade_no);
                if (payResult!=null && "FAIL".equals(payResult.get("return_code"))){
                    if ("ORDERPAID".equals(payResult.get("err_code"))){
                        result = new Result(true,"支付成功");
                        //保存订单
                        seckillOrderService.saveOrderFromRedisToDb(userName,Long.valueOf(out_trade_no),map.get("transaction_id"));
                    }
                }
                //删除订单
                if (result.isSuccess()==false){
                    seckillOrderService.deleteOrderFromRedis(userName,Long.valueOf(out_trade_no));
                }

                break;
            }
        }
       return result;
    }
}
