package com.ads.assignment1.dbaccess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ads.assignment1.dbaccess.Pojos.UserInsertSearchRecord;
import com.ads.assignment1.dbaccess.Pojos.UserSearchHistory;
import com.ads.assignment1.dbaccess.Pojos.UserStatus;
import com.ads.assignment1.dbaccess.entity.Place;
import com.ads.assignment1.dbaccess.entity.SearchHistory;
import com.ads.assignment1.dbaccess.entity.User;
import com.ads.assignment1.dbaccess.repository.HistoryRepository;
import com.ads.assignment1.dbaccess.repository.PlaceRepository;
import com.ads.assignment1.dbaccess.repository.UserRepository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
// Service layer handles business module
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HistoryRepository historyRepository;
    
    @Autowired
    private PlaceRepository placeRepository;

    @PersistenceContext
    EntityManager em;
    
    public UserService(){

    }
    // Save user details 
    public User saveUser(User objUser) {
        return userRepository.save(objUser);
    }
    
    // Insert user details 
    public String InsertUserDetails(String userUniqueId, String userCreatedAt ){
        return userRepository.InsertUserDetails(userUniqueId, userCreatedAt);
    }
    
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    
    public List<Place> getLocations() {
        return placeRepository.findAll();
    }
    
    // Insert user history details - Debug
    /*
    public boolean InsertUserSearchRecord(UserInsertSearchRecord obj ){
        return historyRepository.InsertUserSearchRecord(obj.getUser_unique_id(), obj.getPlace_name(),
        		obj.getData_link(),obj.getSearched_time(), obj.getLocation_searched_at());
    }*/
    
    @Transactional(readOnly = false)
    public List<UserStatus> InsertUserSearchRecord(UserInsertSearchRecord obj ){
    	StoredProcedureQuery spq = em.createNamedStoredProcedureQuery("myapp.InsertUserSearchRecord");
		spq.setParameter("unique_id", obj.getUser_unique_id());
		spq.setParameter("place_name", obj.getPlace_name());
		spq.setParameter("data_link", obj.getData_link());
		spq.setParameter("searched_time", obj.getSearched_time());
		spq.setParameter("location_searched_at", obj.getLocation_searched_at());
        spq.execute();
		// TODO Auto-generated method stub
        return spq.getResultList();
    }
    
    
	@Transactional(readOnly = true)
	public List<SearchHistory> getHistory(){
		return historyRepository.getHistory();
	}
	
	@Transactional(readOnly = true)
	public List<String> getUserHistory(String userUniqueId){
		
		return historyRepository.ProcGetUserSearchHistory(userUniqueId);
    }
	
	
	// Display user history details 
	@Transactional(readOnly = true)
	public List<UserSearchHistory> getUserSearchHistory(String userUniqueId){
		
        StoredProcedureQuery spq = em.createNamedStoredProcedureQuery("myapp.GetUserSearchHistory");
        spq.setParameter("unique_id", userUniqueId);
        spq.execute();
        return spq.getResultList();
	}
	
	@Transactional(readOnly = false)
	public List<UserStatus> InsertUser(String user_unique_id, String modified_at) {
		StoredProcedureQuery spq = em.createNamedStoredProcedureQuery("myapp.InsertUser");
		spq.setParameter("unique_id", user_unique_id);
		spq.setParameter("modified_at", modified_at);
        spq.execute();
		// TODO Auto-generated method stub
        return spq.getResultList();
	}
	 
    /*  -- Debug
    public User getUserByName(String unique_user_id) {
        return repository.findByName(unique_user_id);
    }

    
    public String deleteProduct(int id) {
        repository.deleteById(id);
        return "product removed !! " + id;
    }
	
    public Product updateProduct(Product product) {
        Product existingProduct = repository.findById(product.getId()).orElse(null);
        existingProduct.setName(product.getName());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setPrice(product.getPrice());
        return repository.save(existingProduct);
    }
    */
}