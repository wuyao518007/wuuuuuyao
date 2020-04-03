package com.ck.furn.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ck.furn.sys.bean.GoodLike;
import com.ck.furn.sys.dao.CommentDao;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class CommentService {
	@Autowired
	private CommentDao commentDao;

	/**
	 * 查询
	 * @param GoodLike
	 * @return
	 */
	public Page<GoodLike> findAllList(GoodLike GoodLike){
		int pageNum = GoodLike.getPageNum();
		int pageAmount = GoodLike.getPageAmount();
		Page<GoodLike> page = PageHelper.startPage(pageNum, pageAmount);
		commentDao.findList(GoodLike);
		return page;
	}
	/**
	 * 添加新
	 * @param GoodLike
	 * @return
	 */
	public int insert(GoodLike GoodLike){
		return commentDao.insert(GoodLike);
	}
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	public int delete(String ids){
		String[] arr = ids.split(",");
		return commentDao.delete(arr);
	}
}
