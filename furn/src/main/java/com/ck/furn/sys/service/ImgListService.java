package com.ck.furn.sys.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ck.furn.sys.bean.ImgList;
import com.ck.furn.sys.dao.ImgListDao;

@Service
@Transactional
public class ImgListService {
	

	@Autowired
	private ImgListDao imgListDao;
	

	/**
	 * 查询图片
	 * @param Img
	 * @return
	 */
	public List<ImgList> findList(ImgList img){
		List<ImgList> list = imgListDao.findList(img);
		return list;
	}
	/**
	 * 从数据库获取图片的byte数组
	 * @param img
	 * @return
	 */
    public ImgList findById(String imgid) {
    	return imgListDao.findById(imgid);
    }
	
	/**
	 * 添加新图片
	 * @param Img
	 * @return
	 */
	public int insert(ImgList img){
		return imgListDao.insert(img);
	}

	 /**
	  * 批量导入新图片
	  * @param users
	  * @return
	  */
	 public int inserts(@Param("imgs") List<ImgList> imgs) {
		 return imgListDao.inserts(imgs);
	 }
	/**
	 * 修改图片
	 * @param Img
	 * @return
	 */
	public int update(ImgList img){
		return imgListDao.update(img);
	}
	
	/**
	 * 删除图片
	 * @param ids
	 * @return
	 */
	public boolean delete(String ids){
		String[] arr = ids.split(",");
		int row=imgListDao.delete(arr);
		boolean tru = false;
		if(ids.length() == row) {
			tru = true;
		}
		return tru;
	}
}
