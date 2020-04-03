package com.ck.furn.common;

import java.util.List;

import com.ck.furn.sys.bean.User;
/**
 * 登录状态实体类（传值到前台）
 *
 */
public class RespBean {
	
	//登录状态(成功为success，失败为error)
	private String status;
	
	//提示信息
	private String msg;
	
	//用户信息
	private User user;

	
	
	public RespBean(String status, String msg, User user) {
		this.status = status;
		this.msg = msg;
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	

}
