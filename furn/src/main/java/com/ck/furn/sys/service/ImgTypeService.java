package com.ck.furn.sys.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ck.furn.sys.bean.ImgType;
import com.ck.furn.sys.bean.User;
import com.ck.furn.sys.dao.ImgTypeDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
@Transactional
public class ImgTypeService {
	

	@Autowired
	private ImgTypeDao imgTypeDao;
	/**
	 * 查询图片类型
	 * @param ImgType
	 * @return
	 */
	public Page<ImgType> findList(ImgType imgType){
		int pageNum = imgType.getPageNum();
		int pageAmount = imgType.getPageAmount();
		Page<ImgType> page = PageHelper.startPage(pageNum, pageAmount);
		imgTypeDao.findList(imgType);
		return page;
	}

	/**
	 * 查询图片类型
	 * @param ImgType
	 * @return
	 */
	public List<ImgType> findAllList(ImgType imgType){
		List<ImgType> page = imgTypeDao.findList(imgType);
		return page;
	}
	/**
	 * 添加新图片类型
	 * @param ImgType
	 * @return
	 */
	public int insert(ImgType imgType){
		return imgTypeDao.insert(imgType);
	}
	
	/**
	 * 修改图片类型
	 * @param ImgType
	 * @return
	 */
	public int update(ImgType imgType){
		return imgTypeDao.update(imgType);
	}
	
	/**
	 * 删除图片类型
	 * @param ids
	 * @return
	 */
	public boolean delete(String ids){
		String[] arr = ids.split(",");
		int row=imgTypeDao.delete(arr);
		boolean tru = false;
		if(ids.length() == row) {
			tru = true;
		}
		return tru;
	}
}
