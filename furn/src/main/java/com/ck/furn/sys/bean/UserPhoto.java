package com.ck.furn.sys.bean;

import com.ck.furn.common.BaseBean;


public class UserPhoto extends BaseBean{

	private String id; //主键
	private int uid;// 
	private byte[] picture; //头像
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public byte[] getPicture() {
		return picture;
	}
	public void setPicture(byte[] picture) {
		this.picture = picture;
	}
	
}
