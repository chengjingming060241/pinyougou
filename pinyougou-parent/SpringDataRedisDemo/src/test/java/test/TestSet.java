package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-redis.xml")
public class TestSet {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void setValue(){
        redisTemplate.boundSetOps("nameSet").add("曹操");
        redisTemplate.boundSetOps("nameSet").add("刘备");
        redisTemplate.boundSetOps("nameSet").add("孙权");
    }


    //没有前后顺序
    @Test
    public void getValue(){
        Set name = redisTemplate.boundSetOps("nameSet").members();
        System.out.println(name);
    }

    //指定值，删除一个
    @Test
    public void removeValue(){
        redisTemplate.boundSetOps("nameSet").remove("刘备");

    }

    //删除所有
    @Test
    public void deleteValue(){
        redisTemplate.delete("nameSet");

    }
}
