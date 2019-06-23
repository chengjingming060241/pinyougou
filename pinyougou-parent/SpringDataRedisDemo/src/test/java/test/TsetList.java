package test;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class TsetList {

    @Autowired
    private RedisTemplate redisTemplate;


    //右压栈：后添加的元素排在后面
    @Test
    public void listValue1(){
        redisTemplate.boundListOps("nameList1").rightPush("刘备");
        redisTemplate.boundListOps("nameList1").rightPush("关羽");
        redisTemplate.boundListOps("nameList1").rightPush("张飞");
        redisTemplate.boundListOps("nameList1").rightPush("张飞");

    }

    //查询右压栈
    @Test
    public void getValue1(){
        List nameList1 = redisTemplate.boundListOps("nameList1").range(0, 10);
        System.out.println(nameList1);
    }


    //左压栈：前进后出
    @Test
    public void listValue2(){
        redisTemplate.boundListOps("nameList2").leftPush("刘备");
        redisTemplate.boundListOps("nameList2").leftPush("关羽");
        redisTemplate.boundListOps("nameList2").leftPush("张飞");
    }

    //查询左压栈
    @Test
    public void getValue2(){
        List nameList1 = redisTemplate.boundListOps("nameList2").range(0, 10);
        System.out.println(nameList1);
    }

    //按索引查询某一个值
    @Test
    public void searchByIndex(){
        String  nameList2 = (String) redisTemplate.boundListOps("nameList2").index(1);
        System.out.println(nameList2);
    }

    //移除指定值
    @Test
    public void remoceValue(){
       redisTemplate.boundListOps("nameList1").remove(1,"关羽");
    }

    //删除所有
    @Test
    public void deleteValue(){
        redisTemplate.delete("nameList2");
    }


}
