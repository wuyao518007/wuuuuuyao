package com.ck.furn.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ck.furn.sys.bean.GoodLike;
import com.ck.furn.sys.bean.Img;

@Mapper
public interface OrderDao {

	/**
	 * 查询
	 * @param GoodLike
	 * @return
	 */
	public List<GoodLike> findList(GoodLike imgType);

	/**
	 *统计查询
	 * @param GoodLike
	 * @return
	 */
	public List<Img> findOrderList(Img img);
	/**
	 * 添加
	 * @param GoodLike
	 * @return
	 */
	public int insert(GoodLike imgType);
	

	
	/**
	 * 修改
	 * @param GoodLike
	 * @return
	 */
	public int updateState(GoodLike imgType);
	
	/**
	 * 删除
	 * @param GoodLike
	 * @return
	 */
	public int delete(@Param("ids") String[] ids);
	

	public int update(@Param("ids") String[] ids);
}
