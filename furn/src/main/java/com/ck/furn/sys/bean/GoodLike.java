package com.ck.furn.sys.bean;

import com.ck.furn.common.BaseBean;

public class GoodLike extends BaseBean{
	private int id; //主键
	private int uid; //用户id
	private String imid; //家具id
	private String createTime; //创建时间
	private String updateTime; //创建时间
	private String context;
	private String name;
	private String keywords;//关键字
	private String imName;
	private String state;
	private String price;
	private String type; //家具类型
	private byte[] image; //主图片
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getImid() {
		return imid;
	}
	public void setImid(String imid) {
		this.imid = imid;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getImName() {
		return imName;
	}
	public void setImName(String imName) {
		this.imName = imName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
