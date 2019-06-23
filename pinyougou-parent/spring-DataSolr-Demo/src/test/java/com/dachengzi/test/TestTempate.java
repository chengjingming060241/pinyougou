package com.dachengzi.test;


import com.dachengzi.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-solr.xml")
public class TestTempate {

    @Autowired
    private SolrTemplate solrTemplate;


    //增加、修改都是solrTemplate.saveBean();
    @Test
    public void testAdd(){
        TbItem item = new TbItem();
        item.setId(1L);
        item.setTitle("华为METE10");
        item.setCategory("手机");
        item.setBrand("手机");
        item.setSeller("华为手机专卖店");
        item.setGoodsId(1L);
        item.setPrice(new BigDecimal(3000.01));

        solrTemplate.saveBean(item);
        //必须加提交，也有回滚
        solrTemplate.commit();
    }

    //根据主键查询
    @Test
    public void findById(){
        TbItem byId = solrTemplate.getById(1L, TbItem.class);
        System.out.println(byId.getTitle());
    }

    //根据主键删除
    @Test
    public void deleteById(){
        solrTemplate.deleteById("1");
        solrTemplate.commit();
    }

    //批量数据增加
    @Test
    public void testAddList(){
        List list = new ArrayList();
        for (int i = 0; i <100 ; i++) {
            TbItem item = new TbItem();
            item.setId(i+1L);
            item.setTitle("华为METE10"+i);
            item.setCategory("手机");
            item.setBrand("手机"+i);
            item.setSeller("华为手机专卖店");
            item.setGoodsId(10L);
            item.setPrice(new BigDecimal(3000.01+i));
            list.add(item);
        }

        solrTemplate.saveBeans(list);
        solrTemplate.commit();
    }

    //分页查询
    @Test
    public void testPageQuery(){
        //查询所有
        Query query = new SimpleQuery("*:*");
        //构建条件 contains：包含
        Criteria criteria = new Criteria("item_category").contains("手机");
        //追加条件 必须 =
        criteria = criteria.and("item_brand").contains("2");
        query.addCriteria(criteria);
        //query.setOffset(20);//开始索引
        //query.setRows(20); //每页记录数

        //分页查询page结果
        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);

        //循环得到所有结果
        for (TbItem item:page.getContent()){
            System.out.println(item.getTitle()+"---"+item.getPrice()+"----"+item.getBrand());
        }

        System.out.println("总记录数："+page.getTotalElements());
        System.out.println("总页数："+page.getTotalPages());

    }

    //删除
    @Test
    public void deleteAll(){

        Query query = new SimpleQuery("*:*");
        solrTemplate.delete(query);

        solrTemplate.commit();
    }
}
