package com.ck.furn.common.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.DigestUtils;

import com.ck.furn.common.RespBean;
import com.ck.furn.sys.bean.User;
import com.ck.furn.sys.service.UserService;
import com.ck.furn.util.UserUtils;

import net.sf.json.JSONObject;



/**
 * Created by 
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;
   

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	//根据传入的userDetailsService方法来接受我们自定义的认证方法，密码使用自定义方法PasswordEncoder()验证
    	auth.userDetailsService(userService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
            }

            /**
             * @param charSequence 明文
             * @param s 密文
             * @return
             */
            @Override
            public boolean matches(CharSequence charSequence, String s) {
            	
            	if(s!=null&&s!=""){
            		return s.equals(DigestUtils.md5DigestAsHex(charSequence.toString().getBytes()));
            	}else{
            		return false;
            	}
                
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	//通过authorizeRequests()方法请求权限配置
        http.authorizeRequests()
                //以/admin/**",/reg开头的url只能让拥有"超级管理员"角色的用户访问---权限设置
                /*.antMatchers("/user/**","/reg").hasRole("1")///admin/**的URL都需要有超级管理员角色，如果使用.hasAuthority()方法来配置，需要在参数中加上ROLE_,如下.hasAuthority("ROLE_超级管理员")
                //未匹配的任何url都需要身份验证
*/              //.anyRequest().authenticated()//其他的路径都是登录后即可访问
                //formLogin()是一种授权认证方式
                .and().formLogin()
                //.loginPage("/login_page")
                .successHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                httpServletResponse.setContentType("application/json;charset=utf-8");
        		//获取当前用户信息
                User user = UserUtils.getCurrentUser();
                //登录状态实体类
                RespBean rb = new RespBean("success","登录成功",user);
                //未审核
                if("院校".equals(user.getRoleType()) && !"是".equals(user.getKeepAlive())) {
                	rb = new RespBean("success","该用户未通过审核，请联系管理员！",new User());
                }
                //将rb转换成json对象
                JSONObject json = JSONObject.fromObject(rb);                
                //session存储
                HttpSession session = httpServletRequest.getSession(true);
                session.setAttribute("user", JSONObject.fromObject(user));
                PrintWriter out = httpServletResponse.getWriter();
                //将就送对象传到前台
                System.out.println("登录成功");
                out.write(json.toString());
                out.flush();
                out.close();
            }
        })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                    	httpServletResponse.setContentType("application/json;charset=utf-8");
                        
                        //向前台传入验证信息（在data中）
                        PrintWriter out = httpServletResponse.getWriter();
                        out.write("{\"status\":\"error\",\"msg\":\"登录失败\"}");
                        out.flush();
                        out.close();
                    }
                }).loginProcessingUrl("/login")
                .usernameParameter("username").passwordParameter("password").permitAll()
                .and().logout().permitAll().and().csrf().disable().exceptionHandling().accessDeniedHandler(getAccessDeniedHandler());
    }

    //免登陆,一般用于设置主页
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/index.jsp");
    }

    @Bean
    AccessDeniedHandler getAccessDeniedHandler() {
        return new AuthenticationAccessDeniedHandler();
    }
}