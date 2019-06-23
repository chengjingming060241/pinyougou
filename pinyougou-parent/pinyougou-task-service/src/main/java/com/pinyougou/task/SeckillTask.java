package com.pinyougou.task;

import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillGoodsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class SeckillTask {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;


    @Scheduled(cron = "0 * * * * ?")
    public void refreshSeckillGoods(){
        System.out.println("执行了秒杀商品增量更新 任务调用"+new Date());

        //查询缓存中的秒杀商品ID集合
        List goodsIds = new ArrayList(redisTemplate.boundHashOps("seckillGoods").keys());
        System.out.println(goodsIds);
        TbSeckillGoodsExample example = new TbSeckillGoodsExample();
        TbSeckillGoodsExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1"); //审核通过的商品
        criteria.andStockCountGreaterThan(0);  //库存大于0的
        criteria.andStartTimeLessThanOrEqualTo(new Date()); //开始日期小于等于当前日期
        criteria.andEndTimeGreaterThanOrEqualTo(new Date()); //当前日期小于等于截止日期

        if (goodsIds.size()>0){
            criteria.andIdNotIn(goodsIds); //排除缓存中已存在的商品id集合
        }

        List<TbSeckillGoods> seckillGoodsList = seckillGoodsMapper.selectByExample(example);
        for (TbSeckillGoods seckillGoods:seckillGoodsList) {
            redisTemplate.boundHashOps("seckillGoods").put(seckillGoods.getId(),seckillGoods);
            System.out.println("增量更新秒杀商品ID"+seckillGoods.getId());
        }
    }

    @Scheduled(cron = "* * * * * ?")
    public void removeSeckillGoods(){
        List<TbSeckillGoods> goodsIds = new ArrayList(redisTemplate.boundHashOps("seckillGoods").values());
        System.out.println("执行秒杀商品的任务");
        for (TbSeckillGoods seckillGoods:goodsIds) {
            if (seckillGoods.getEndTime().getTime()<new Date().getTime()){
                //同步数据库
                seckillGoodsMapper.updateByPrimaryKey(seckillGoods);
                //清楚缓存
                redisTemplate.boundHashOps("seckillGoods").delete(seckillGoods.getId());
                System.out.println("秒杀商品"+seckillGoods.getId()+"已过期");
            }
        }
    }
}
