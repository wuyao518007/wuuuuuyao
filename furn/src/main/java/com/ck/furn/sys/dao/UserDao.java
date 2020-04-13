/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.ck.furn.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ck.furn.sys.bean.User;
import com.ck.furn.sys.bean.UserPhoto;


/**
 * 用户DAO接口
 * @version 
 */
@Mapper
public interface UserDao{

    User loadUserByUsername(@Param("loginName") String loginName);
	/**
	 * 查询人员照片
	 * @param user
	 * @return
	 */
	public List<User> findList(User user);
	
	/**
	 * 按id查找人员信息
	 * @param user
	 * @return
	 */
	public User findById1(User user);	

	/**
	 * 从数据库获取图片的byte数组
	 * @param img
	 * @return
	 */
    public User findById(@Param("id") int id);
	/**
	 * 添加新用户
	 * @param user
	 * @return
	 */
	public int insert(User user);
	
	/**
	 * 修改用户
	 * @param user
	 * @return
	 */
	public int update(User user);


	/**
	 * 修改密码
	 * @param user
	 * @return
	 */
	public int updatePassWord(User user);
	 /**
	  * 批量导入用户
	  * @param users
	  * @return
	  */
    public int insertUsers(@Param("users") List<User> users);
	
	/**
	 * 删除用户
	 * @param ids
	 * @return
	 */
	public int delete(@Param("ids") String[] ids);
	

	 /**
	  * 导入用户头像
	  * @param users
	  * @return
	  */
	public int insertPhoto(UserPhoto userPhoto);
	/**
	 * 删除用户头像
	 * @param ids
	 * @return
	 */
	public int deletePhoto(@Param("id") String id);
}
