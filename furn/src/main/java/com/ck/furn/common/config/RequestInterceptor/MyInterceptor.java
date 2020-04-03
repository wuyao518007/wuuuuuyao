package com.ck.furn.common.config.RequestInterceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONObject;

/**
 * 拦截器
 * @author ck
 *
 */
public class MyInterceptor implements HandlerInterceptor{

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		//在整个请求之后，渲染视图之后执行，主要用于资源清理工作
		 JSONObject object = new JSONObject();
		 try {
			/*
			 * RequestWrapper requestWrapper = new RequestWrapper(arg0); String body =
			 * requestWrapper.getSessionStream(); if(!"".equals(body)) { object =
			 * JSONObject.fromObject(body); System.out.println(body); }
			 */
        }catch (Exception e){
			/* e.printStackTrace(); */
        }
   		 Map<String, String> params = new HashMap<String,String>();  
		System.out.println("在整个请求之后，渲染视图之后执行，主要用于资源清理工作");		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		//请求处理之后进行调用，但是在视图被渲染之前，即controller方法调用之后
		System.out.println("请求处理之后进行调用，但是在视图被渲染之前，即controller方法调用之后");
	}

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2) throws Exception {
		// TODO Auto-generated method stub
		//controller方法调用之前
		System.out.println("controller方法调用之前");
		return true;
	}

}
