package com.pinyougou.search.service;


import java.util.List;
import java.util.Map;

public interface ItemSearchService {

    /**
     * 搜索方法
     * @param searchMap
     * @return
     */
    Map search(Map searchMap);

    /**
     * 导入类表
     * @param list
     */
    void importList(List list);

    /**
     * 删除商品列表
     * @param goodsIds
     */
    void deleteByGoodsIds(List goodsIds);
}
