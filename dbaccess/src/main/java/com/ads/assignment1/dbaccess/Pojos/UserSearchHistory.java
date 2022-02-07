package com.ads.assignment1.dbaccess.Pojos;

import java.sql.Timestamp;


public class UserSearchHistory {

	private String place_name;
	private String data_link;
	private String searched_time;
	private String location_searched_at;
	
	public UserSearchHistory(String place_name, String data_link,String searched_time, String location_searched_at) {
		this.place_name = place_name;
		this.data_link = data_link;
		this.searched_time = searched_time;
		this.location_searched_at = location_searched_at;
	}

	public String getPlace_name() {
		return place_name;
	}

	public void setPlace_name(String place_name) {
		this.place_name = place_name;
	}

	public String getData_link() {
		return data_link;
	}

	public void setData_link(String data_link) {
		this.data_link = data_link;
	}

	public String getSearched_time() {
		return searched_time;
	}

	public void setSearched_time(String searched_time) {
		this.searched_time = searched_time;
	}

	public String getLocation_searched_at() {
		return location_searched_at;
	}

	public void setLocation_searched_at(String location_searched_at) {
		this.location_searched_at = location_searched_at;
	}
	
}
