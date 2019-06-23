package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pay.service.WeiXinPayService;
import com.pinyougou.pojo.TbPayLog;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.IdWorker;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PayController {

    @Reference(timeout = 10000)
    private WeiXinPayService weiXinPayService;

    @Reference
    private OrderService orderService;


    @RequestMapping("/createNative")
    public Map createNative(){
        //获取当前登录用户
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        //从缓存中提取支付日志
        TbPayLog payLog = orderService.searchPayLogFromRedis(name);
        //调用微信支付接口
        if (null!=payLog){
            return weiXinPayService.createNative(payLog.getOutTradeNo(),payLog.getTotalFee()+"");
        }else {
            return new HashMap();
        }
    }


    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String out_trade_no){
        Result result = null;
        int x = 0;
        while (true){
            Map<String,String> map = weiXinPayService.queryPayStatus(out_trade_no); //调用查询
            if (map==null){
                result = new Result(false,"支付发送错误");
                break;
            }
            if(map.get("trade_state").equals("SUCCESS")) {
                result = new Result(true,"支付成功");
                orderService.updateOrderStatus(out_trade_no,map.get("transaction_id"));  //修改支付状态
                break;
            }
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        x++;
        if (x>=100){
            result = new Result(false,"支付超时");

        }

        return result;
    }
}
