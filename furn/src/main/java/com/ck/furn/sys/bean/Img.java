package com.ck.furn.sys.bean;

import java.util.List;

import com.ck.furn.common.BaseBean;

public class Img extends BaseBean{

	private String id; //主键
	private String name; //名称
	private List<ImgList> picture; //图片
	private byte[] image; //主图片
	private String type; //类型
	private String url; //地址
	private String remark; //简介
	private int price;//零售价
	private int jprice;//进货价
	private int kc;//库存
	private int xl;//销量
	private int num;//进货量
	private String state;
	private int gid;
	private int fid;
	private int uid;
	
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public int getFid() {
		return fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public void setPicture(List<ImgList> picture) {
		this.picture = picture;
	}
	public List<ImgList> getPicture() {
		return picture;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getJprice() {
		return jprice;
	}
	public void setJprice(int jprice) {
		this.jprice = jprice;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getKc() {
		return kc;
	}
	public void setKc(int kc) {
		this.kc = kc;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getXl() {
		return xl;
	}
	public void setXl(int xl) {
		this.xl = xl;
	}
	
}
