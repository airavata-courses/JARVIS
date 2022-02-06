package com.ads.assignment1.dbaccess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.ads.assignment1.dbaccess.Pojos.UserInsertSearchRecord;
import com.ads.assignment1.dbaccess.entity.SearchHistory;

@Repository
public interface HistoryRepository  extends JpaRepository<SearchHistory, Long> {

	@Procedure(procedureName = "myapp.getHistory")
	List<SearchHistory> getHistory( );

	@Procedure(procedureName = "myapp.GetUserSearchHistory")
	List<String> ProcGetUserSearchHistory( String unique_user_id);
	
	@Procedure(procedureName = "myapp.InsertUserSearchRecord")
	boolean InsertUserSearchRecord( String userUniqueId , String placeName,String dataLink , String searchedTime);
	
	
}
