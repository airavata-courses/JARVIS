package com.ads.assignment1.dbaccess.controller;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import com.ads.assignment1.dbaccess.Pojos.UserInsertSearchRecord;
import com.ads.assignment1.dbaccess.Pojos.UserSearchHistory;
import com.ads.assignment1.dbaccess.Pojos.UserStatus;
import com.ads.assignment1.dbaccess.entity.Place;
import com.ads.assignment1.dbaccess.entity.SearchHistory;
import com.ads.assignment1.dbaccess.entity.User;
import com.ads.assignment1.dbaccess.service.RestService;
import com.ads.assignment1.dbaccess.service.UserService;
import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UserController {

    @Autowired
    private UserService service;
    @Autowired
    private RestService restService;
    
    public boolean return_status;
    
    // Add new user details to user_master table 
    @PostMapping("addUser")
    public List<UserStatus> InsertUserDetails(@RequestBody User objUser ){
    	
        //return service.InsertUserDetails(objUser.getUser_unique_id(), objUser.getModified_at());
    	return service.InsertUser(objUser.getUser_unique_id(), objUser.getModified_at());
    }
    
    // Get all the user list
    @GetMapping("/getAllUsers")
    public List<User> findAllUsers() {
        return service.getUsers();
    }
    
    // Get all the place list
    @GetMapping("/locations")
    public List<Place> getLocations() {
        return service.getLocations();
    }
    
    // Get all the history list
	@GetMapping("getHistory")
	public List<SearchHistory> getHistory(){
		return service.getHistory();
	}
	
	// Add new history searched record by user to history_master
    @PostMapping("addUserSearchRecord")
    public List<UserStatus> InsertUserDetails(@RequestBody UserInsertSearchRecord obj ){
    	return service.InsertUserSearchRecord(obj);
    }
    /*
    @PostMapping("addUserSearchRecord")
    public boolean InsertUserDetails(@RequestBody UserInsertSearchRecord obj ){
    	return service.InsertUserSearchRecord(obj);
    }
    */
    
    @GetMapping("getUserSearchHistory")
	public void getUserUniqueID(@RequestParam String sessionId)
	{
		String output = restService.createPost(sessionId);
		System.out.println(output);
	}
   /*
    // Normal Array String
     
	@PostMapping("getUserSearchHistory")
	public List<String> getUserHistory(@RequestBody User objUser ){
		
		return service.getUserHistory(objUser.getUser_unique_id());
    }
	
    */
     // JSON return string
    /*
	@GetMapping("getUserSearchHistory/{id}")
	public List<UserSearchHistory> getUserHistory(@PathVariable ( value = "id") String id ){
		return service.getUserSearchHistory(id);
    }
    */
	
    // Get history searched record by user
	@PostMapping("getUserSearchHistory1")
	public List<UserSearchHistory> getUserHistory(@RequestBody User objUser  ){
		return service.getUserSearchHistory(objUser.getUser_unique_id());
    }
	
}