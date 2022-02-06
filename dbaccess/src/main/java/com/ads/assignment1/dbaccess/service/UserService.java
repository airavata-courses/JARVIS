package com.ads.assignment1.dbaccess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ads.assignment1.dbaccess.Pojos.UserInsertSearchRecord;
import com.ads.assignment1.dbaccess.Pojos.UserSearchHistory;
// import com.ads.assignment1.dbaccess.Pojos.UserSearchHistory;
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
    public User saveUser(User objUser) {
        return userRepository.save(objUser);
    }
    
    public boolean InsertUserDetails(String userUniqueId ){
        return userRepository.InsertUserDetails(userUniqueId);
    }
    
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    
    public List<Place> getLocations() {
        return placeRepository.findAll();
    }
    
    public boolean InsertUserSearchRecord(UserInsertSearchRecord obj ){
        return historyRepository.InsertUserSearchRecord(obj.getUser_unique_id(), obj.getPlace_name(),
        		obj.getData_link(),obj.getSearched_time());
    }
    
	@Transactional(readOnly = true)
	public List<SearchHistory> getHistory(){
		return historyRepository.getHistory();
	}
	
	@Transactional(readOnly = true)
	public List<String> getUserHistory(String userUniqueId){
		
		return historyRepository.ProcGetUserSearchHistory(userUniqueId);
    }
	
	@Transactional(readOnly = true)
	public List<UserSearchHistory> getUserHistory1(String userUniqueId){
		
        StoredProcedureQuery spq = em.createNamedStoredProcedureQuery("myapp.GetUserSearchHistory");
        spq.setParameter("unique_id", userUniqueId);
        spq.execute();
        return spq.getResultList();
	}
	
    /*
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