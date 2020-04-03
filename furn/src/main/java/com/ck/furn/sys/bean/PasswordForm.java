package com.ck.furn.sys.bean;

/**
 * PasswordForm
 *
 * @author bobbi
 * @date 2018/10/26 17:40
 * @email 571002217@qq.com
 * @description
 */
public class PasswordForm {

	private int id; //主键
    private String password;
    private String newPassword;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
