package com.ads.assignment1.dbaccess.Pojos;

import java.sql.Timestamp;


public class UserSearchHistory {

	private String place_name;
	private String data_link;
	private String searched_time;
	private Timestamp created_at;
	public UserSearchHistory(String place_name, String data_link,String searched_time, Timestamp created_at) {
		this.place_name = place_name;
		this.data_link = data_link;
		this.searched_time = searched_time;
		this.created_at = created_at;
	}
	
}
