package com.ck.furn.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ck.furn.sys.bean.ImgList;
import com.ck.furn.sys.bean.User;

@Mapper
public interface ImgListDao {
	/**
	 * 查询图片
	 * @param Img
	 * @return
	 */
	public List<ImgList> findList(ImgList imgList);
	
	/**
	 * 从数据库获取图片的byte数组
	 * @param img
	 * @return
	 */
    public ImgList findById(@Param("id") String imgid);
	
	/**
	 * 添加新图片
	 * @param Img
	 * @return
	 */
	public int insert(ImgList img);

	 /**
	  * 批量导入新图片
	  * @param users
	  * @return
	  */
   public int inserts(@Param("imgs") List<ImgList> imgs);
	
	/**
	 * 修改图片
	 * @param Img
	 * @return
	 */
	public int update(ImgList imgList);
	
	
	/**
	 * 删除图片
	 * @param ids
	 * @return
	 */
	public int delete(@Param("ids") String[] ids);
}
