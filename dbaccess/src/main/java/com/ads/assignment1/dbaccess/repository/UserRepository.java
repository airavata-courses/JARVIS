package com.ads.assignment1.dbaccess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import com.ads.assignment1.dbaccess.entity.User;

//user_master repository
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
	
	@Procedure(procedureName = "myapp.InsertUser")
	String InsertUserDetails( String unique_user_id, String user_created_at);
	
}