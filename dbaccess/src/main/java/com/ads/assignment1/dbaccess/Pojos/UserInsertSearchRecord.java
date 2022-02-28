package com.ads.assignment1.dbaccess.Pojos;
//Class is used to searched result set mapping 
public class UserInsertSearchRecord {

	private String user_unique_id;
	private String place_name;
	private String data_link;
	private String searched_time;
	private String location_searched_at;
	
	public UserInsertSearchRecord(String user_unique_id,String place_name, String data_link, String searched_time , String location_searched_at) {
		this.user_unique_id = user_unique_id;
		this.place_name = place_name;
		this.data_link = data_link;
		this.searched_time = searched_time;
		this.location_searched_at=location_searched_at;
	}
	// Setter and getter of each column
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

	public String getUser_unique_id() {
		return user_unique_id;
	}

	public void setUser_unique_id(String user_unique_id) {
		this.user_unique_id = user_unique_id;
	}
}
