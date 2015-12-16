package com.glm.bean;

/**
 * Contiene i parametri per i vari Social Account disponibili
 * **/
public class SocialAccount {
	/**
	 * twitter/facebook/google buzz
	 * **/
	private String sSocialType;
	
	private String sSocialUser;
	
	private String sSocialPWD;

	public synchronized String getsSocialType() {
		return sSocialType;
	}

	public synchronized void setsSocialType(String sSocialType) {
		this.sSocialType = sSocialType;
	}

	public synchronized String getsSocialUser() {
		return sSocialUser;
	}

	public synchronized void setsSocialUser(String sSocialUser) {
		this.sSocialUser = sSocialUser;
	}

	public synchronized String getsSocialPWD() {
		return sSocialPWD;
	}

	public synchronized void setsSocialPWD(String sSocialPWD) {
		this.sSocialPWD = sSocialPWD;
	}
	
}
