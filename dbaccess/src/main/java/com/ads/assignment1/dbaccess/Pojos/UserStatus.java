package com.ads.assignment1.dbaccess.Pojos;

public class UserStatus {
	private String status;
	private String message;
	
	public UserStatus(String status, String message) {
		this.status = status;
		this.message = message;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
