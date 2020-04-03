package com.ck.furn.sys.bean;

import com.ck.furn.common.BaseBean;

public class ImgType extends BaseBean{

	private String id; //主键
	private String type; //类型
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
