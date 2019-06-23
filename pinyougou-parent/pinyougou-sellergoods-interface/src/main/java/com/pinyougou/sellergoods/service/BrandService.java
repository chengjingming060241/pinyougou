package com.pinyougou.sellergoods.service;

import java.util.List;
import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

/**
 * 品牌接口
 * @author Administrator
 *
 */
public interface BrandService {

	/**
	 * 获取品牌数据
	 * @return
	 */
	List<TbBrand> findAll();

	/**
	 * 品牌分区
	 * @param pageNum 当前页码
	 * @param pageSize 每页记录数
	 * @return
	 */
	PageResult findPage(int pageNum,int pageSize);

	/**
	 * 增加
	 * @param tbBrand
	 */
	void add(TbBrand tbBrand);

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	TbBrand findOne(Long id);

	/**
	 * 修改
	 * @param brand
	 */
	void update(TbBrand brand);

	/**
	 * 删除
	 * @param ids
	 */
	void delete(Long[] ids);

	/**
	 * 查询
	 * @param brand
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	PageResult findPage(TbBrand brand, int pageNum,int pageSize);

	/**
	 * 返回下拉列表
	 * @return
	 */
	List<Math> selectOptionList();
}
