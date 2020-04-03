package com.ck.furn.common.config.RequestInterceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 拦截需要从request流中读取参数的请求地址
 * @author ck
 *
 */
@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter{

	public void addInterceptors(InterceptorRegistry registry) {
		//registry.addInterceptor(new MyInterceptor()).addPathPatterns("/grgzt/findWdgzList");//有多个拦截器继续add进去
		registry.addInterceptor(new MyInterceptor()).addPathPatterns("/sjzcst/getSjzl");//数据资产视图
		registry.addInterceptor(new MyInterceptor()).addPathPatterns("/fwjzml/getDsjzxfwl");//服务价值目录
		registry.addInterceptor(new MyInterceptor()).addPathPatterns("/sjzclt/getXtdy");//数据资产流图
		registry.addInterceptor(new MyInterceptor()).addPathPatterns("/sjzcml/getAzttj");//数据资产目录
		registry.addInterceptor(new MyInterceptor()).addPathPatterns("/user/findUserList");//安全管理-用户
		registry.addInterceptor(new MyInterceptor()).addPathPatterns("/role/findRoleList");//安全管理-角色
		registry.addInterceptor(new MyInterceptor()).addPathPatterns("/office/findOfficeList");//安全管理-部门
		super.addInterceptors(registry);
	}
}
