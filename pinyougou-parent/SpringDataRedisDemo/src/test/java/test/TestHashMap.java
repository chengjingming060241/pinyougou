package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class TestHashMap {

    @Autowired
    private RedisTemplate redisTemplate;


    //存值
    @Test
    public void setValue(){
        redisTemplate.boundHashOps("nameMap").put("a","唐僧");
        redisTemplate.boundHashOps("nameMap").put("b","孙悟空");
        redisTemplate.boundHashOps("nameMap").put("c","八戒");
        redisTemplate.boundHashOps("nameMap").put("d","沙僧");
    }

    //获取所有Key
    @Test
    public void getKeys(){
        Set nameMap = redisTemplate.boundHashOps("nameMap").keys();
        System.out.println(nameMap);
    }

    //获取所有值
    @Test
    public void getValue(){
        List nameMap = redisTemplate.boundHashOps("nameMap").values();
        System.out.println(nameMap);
    }

    //根据K获取所有值
    @Test
    public void searchValue(){
        String nameMap = (String)redisTemplate.boundHashOps("nameMap").get("b");
        System.out.println(nameMap);
    }

    //移除某个小K的值
    @Test
    public void remoceValue(){
       redisTemplate.boundHashOps("nameMap").delete("c");
    }
}
