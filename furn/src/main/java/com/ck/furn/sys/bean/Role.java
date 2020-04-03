package com.ck.furn.sys.bean;

import com.ck.furn.common.BaseBean;

/**
 * 权限
 * @author ck
 * @version 2019-12-7
 */
public class Role extends BaseBean{

    private int id;
    private String name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
}
