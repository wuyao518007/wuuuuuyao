package com.ck.furn.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ck.furn.sys.bean.Img;
import com.ck.furn.sys.bean.RestBean;

@Mapper
public interface ImgDao {
	/**
	 * 查询图片
	 * @param Img
	 * @return
	 */
	public List<Img> findList(Img img);
	
	/**
	 * 查询图片
	 * @param Img
	 * @return
	 */
	public List<Img> findUserList(Img img);
	
	/**
	 * 从数据库获取图片的byte数组
	 * @param img
	 * @return
	 */
    public Img findById(@Param("imgid") String imgid);
	
	/**
	 * 添加新图片
	 * @param Img
	 * @return
	 */
	public int insert(Img img);
	
	/**
	 * 修改图片
	 * @param Img
	 * @return
	 */
	public int update(Img img);

	/**
	 * 更新库存
	 * @param Img
	 * @return
	 */
	public int updatekc(@Param("list") List<RestBean> list);
	
	/**
	 * 删除图片
	 * @param ids
	 * @return
	 */
	public int delete(@Param("ids") String[] ids);
}
