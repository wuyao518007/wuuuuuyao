package com.ck.furn.common;
/**
 * 基类
 * @author ck
 *
 */
public class BaseBean {
	//当前页码
	private int pageNum;
	
	//默认每页数据量
	private int pageAmount;

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageAmount() {
		return pageAmount;
	}

	public void setPageAmount(int pageAmount) {
		this.pageAmount = pageAmount;
	}

	
	
	
}

