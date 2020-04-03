package com.ck.furn.sys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ck.furn.sys.bean.Img;
import com.ck.furn.sys.bean.RestBean;
import com.ck.furn.sys.bean.User;
import com.ck.furn.sys.dao.ImgDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
@Transactional
public class ImgService {
	

	@Autowired
	private ImgDao imgDao;
	/**
	 * 查询图片
	 * @param Img
	 * @return
	 */
	public Page<Img> findList(Img img){
		int pageNum = img.getPageNum();
		int pageAmount = img.getPageAmount();
		Page<Img> page = PageHelper.startPage(pageNum, pageAmount);
		imgDao.findList(img);
		return page;
	}
	

	/**
	 * 查询图片
	 * @param Img
	 * @return
	 */
	public Page<Img> findUserList(Img img){
		int pageNum = img.getPageNum();
		int pageAmount = img.getPageAmount();
		Page<Img> page = PageHelper.startPage(pageNum, pageAmount);
		imgDao.findUserList(img);
		return page;
	}
	

	/**
	 * 查询图片
	 * @param Img
	 * @return
	 */
	public List<Img> findListById(Img img){
		List<Img> list = imgDao.findUserList(img);
		return list;
	}
	/**
	 * 从数据库获取图片的byte数组
	 * @param img
	 * @return
	 */
    public Img findById(String imgid) {
    	return imgDao.findById(imgid);
    }
	
	/**
	 * 添加新图片
	 * @param Img
	 * @return
	 */
	public int insert(Img img){
		return imgDao.insert(img);
	}
	
	/**
	 * 修改图片
	 * @param Img
	 * @return
	 */
	public int update(Img img){
		return imgDao.update(img);
	}
	public static void main(String[] args) {
		Map<String, Integer> map= new HashMap<String, Integer>();
		System.out.println(map.get("123"));
		
	}
	/**
	 * 更新库存
	 * @param ids
	 * @return
	 */
	public boolean updatekc(String ids){
		String[] arr = ids.split(",");
		List<RestBean> list = new ArrayList<RestBean>();
//		Map<String, Integer> map= new HashMap<String, Integer>();
//		for(String id:arr) {
//			if(map.get(id) ==null) {
//				map.put(id,1);
//			}else {
//				map.put(id,map.get(id)+1);
//			}
//		}
		Map<String,RestBean> map= new HashMap<String,RestBean>();
		
		for(String id:arr) {
			if(map.get(id) ==null) {
				RestBean bean= new RestBean();
				map.put(id,bean);
				bean.setId(id);;
				bean.setNum(1);
			}else {
				RestBean bean= map.get(id);
				int num = bean.getNum();
				bean.setNum(num+1);
			}
		}
		for(String key:map.keySet()) {
			list.add(map.get(key));
		}
		int row=imgDao.updatekc(list);
		boolean tru = false;
		if(ids.length() == row) {
			tru = true;
		}
		return tru;
	}
	/**
	 * 删除图片
	 * @param ids
	 * @return
	 */
	public boolean delete(String ids){
		String[] arr = ids.split(",");
		int row=imgDao.delete(arr);
		boolean tru = false;
		if(ids.length() == row) {
			tru = true;
		}
		return tru;
	}
}
