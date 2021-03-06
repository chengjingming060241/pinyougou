package com.pinyougou.solrutil;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class SolrUtil {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private SolrTemplate solrTemplate;

    //批量导入
    public void importItemData(){
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1"); //审核通过才导入
        List<TbItem> items = itemMapper.selectByExample(example);

        System.out.println("----------商品列表-----------");
        for (TbItem item:items) {
            Date updateTime = item.getUpdateTime();
            System.out.println(item.getId()+""+item.getTitle()+" "+item.getBrand()+" "+updateTime);

            Map map = JSON.parseObject(item.getSpec(), Map.class);

            item.setSpecMap(map);
        }

        solrTemplate.saveBeans(items);
//        Query query = new SimpleQuery("*:*");
//        solrTemplate.delete(query);
        solrTemplate.commit();
        System.out.println("----------结束-----------");
    }

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        SolrUtil solrUtil = (SolrUtil) context.getBean("solrUtil");
        solrUtil.importItemData();
    }

}
