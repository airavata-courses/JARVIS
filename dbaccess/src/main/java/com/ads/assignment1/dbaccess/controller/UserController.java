package com.ads.assignment1.dbaccess.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import com.ads.assignment1.dbaccess.Pojos.UserInsertSearchRecord;
import com.ads.assignment1.dbaccess.Pojos.UserSearchHistory;
import com.ads.assignment1.dbaccess.entity.Place;
import com.ads.assignment1.dbaccess.entity.SearchHistory;
import com.ads.assignment1.dbaccess.entity.User;
import com.ads.assignment1.dbaccess.service.UserService;

import java.sql.Timestamp;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService service;
    /*
    @PostMapping("addUser")
    public User addProduct(@RequestBody User objUser) {
		Timestamp time = new Timestamp(System.currentTimeMillis());
		objUser.setModified_at(time);
        return service.saveUser(objUser);
    }
    */
    
    @PostMapping("addUser")
    public boolean InsertUserDetails(@RequestBody User objUser ){
        return service.InsertUserDetails(objUser.getUser_unique_id());
    }
    
    
    @GetMapping("/getAllUsers")
    public List<User> findAllUsers() {
        return service.getUsers();
    }
    
    @GetMapping("/locations")
    public List<Place> getLocations() {
        return service.getLocations();
    }
    
	@GetMapping("getHistory")
	public List<SearchHistory> getHistory(){
		return service.getHistory();
	}
	
    @PostMapping("addUserSearchRecord")
    public boolean InsertUserDetails(@RequestBody UserInsertSearchRecord obj ){
        return service.InsertUserSearchRecord(obj);
    }
    
	@GetMapping("getUserSearchHistory")
	public List<String> getUserHistory(@RequestBody User objUser ){
		return service.getUserHistory(objUser.getUser_unique_id());
    }
	
	@GetMapping("getUserSearchHistory1")
	public List<UserSearchHistory> getUserHistory1(){
		return service.getUserHistory1("Snehal");
    }
	
}