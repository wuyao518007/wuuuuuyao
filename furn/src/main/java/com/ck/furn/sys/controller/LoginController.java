package com.ck.furn.sys.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ck.furn.sys.bean.User;
import com.ck.furn.sys.bean.UserPhoto;
import com.ck.furn.sys.service.LoginService;
import com.ck.furn.sys.service.UserService;
import com.ck.furn.util.ConstValues;
import com.ck.furn.util.DateUtils;
import com.ck.furn.util.IdGen;
import com.ck.furn.util.MD5Util;
import com.ck.furn.util.ReturnStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 用户登录
 */
@RestController
@RequestMapping("/user")
@Api(value = "登录的操作类")
public class LoginController {
	@Autowired
	private LoginService loginService;
	@Autowired
	private UserService userService;

	/**
	 * 用户登录
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "用户登录")
	public ReturnStatus login (User user,HttpServletRequest request,HttpServletResponse response) {
		ReturnStatus ret = new ReturnStatus(ConstValues.FAILURE,"登录失败!用户名或密码错误");
		
		HttpSession session = request.getSession();
		String pass =MD5Util.getMD5(user.getPassWord());
		user.setPassWord(pass);
		User u= loginService.login(user);
		if(u!= null) {
			if("是".equals(u.getKeepAlive())) {
				ret.setReturnValue(ConstValues.FAILURE);
				ret.setMessage("账号被封!请联系管理员");
				return ret;
			}
			ret.setReturnValue(ConstValues.SUCCESS);
			ret.setObj(u);
			ret.setMessage("登录成功!");
			session.setAttribute("user", u);
			
		}
		return ret;
	}
	
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/resign", method = RequestMethod.POST)
    @ResponseBody
	@ApiOperation(value = "用户注册")
    public ReturnStatus resign(User user,
    		@RequestParam(value = "file", required = false) MultipartFile file){
    	ReturnStatus ret = new ReturnStatus(ConstValues.FAILURE,"注册失败!");   
	
    	List<User> row1=loginService.loginName(user);
		if(row1.size() == 1) {
			ret.setReturnValue(ConstValues.FAILURE);
			ret.setMessage("用户名已存在!");
			return ret;			
		}
		String pass =MD5Util.getMD5(user.getPassWord());
		user.setPassWord(pass);
		user.setCreateTime(DateUtils.getDateTime());
		user.setUpdateTime(DateUtils.getDateTime());
		String pid=IdGen.uuid();
		user.setPid(pid);
		int row=loginService.resign(user);
		if(row == 1) {
			User us = userService.findById1(user);
			if(file != null) {
				UserPhoto userPhoto = new UserPhoto();
				try {
					byte[] data = file.getBytes();
					userPhoto.setPicture(data);
					userPhoto.setId(pid);
					userPhoto.setUid(us.getId());
					int row2=userService.insertPhoto(userPhoto);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			ret.setReturnValue(ConstValues.SUCCESS);
			ret.setMessage("注册成功!");
		}
		return ret;		
    }
	
	/**
	 * 重置密码
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/resetPassWord", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "重置密码")
	public ReturnStatus resetPassWord (User user) {	
		ReturnStatus ret = new ReturnStatus(ConstValues.FAILURE,"重置密码失败!");
		ret.setReturnValue(ConstValues.FAILURE);
		String pass =MD5Util.getMD5(user.getPassWord());
		user.setPassWord(pass);
		int row = loginService.resetPassWord(user);
		if(row == 1) {
			ret.setReturnValue(ConstValues.SUCCESS);
			ret.setMessage("重置密码成功!");
		}
		return ret;
	}
}
