package com.ck.furn.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ck.furn.sys.bean.User;

/**
 * 用户登录
 * @author ck
 * @version 2019-12-7
 */
@Mapper
public interface LoginDao {

	/**
	 * 用户登录
	 * @param user
	 * @return
	 */
	public User login (User user);

	/**
	 * 用户名验证 
	 * @param user
	 * @return
	 */
	public List<User> loginName (User user);
	
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	public int resign (User user);
	
	/**
	 * 重置密码
	 * @param user
	 * @return
	 */
	public int resetPassWord (User user);
	
}
