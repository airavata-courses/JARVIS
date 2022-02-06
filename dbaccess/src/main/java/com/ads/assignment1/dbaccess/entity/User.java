package com.ads.assignment1.dbaccess.entity;



import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "myapp.InsertUser", procedureName = "myapp.InsertUser",
			parameters = {
			    @StoredProcedureParameter(mode = ParameterMode.IN, name="userUniqueId", type = String.class),
			    @StoredProcedureParameter(mode = ParameterMode.OUT, name="insertuser", type = boolean.class)
			})
})

@Table(name = "user_master", schema = "myapp")
public class User {

    @Id
    @GeneratedValue
    private int user_id;
    private String user_unique_id;
    private Integer status;
	private Integer modified_by;
    private Timestamp modified_at;
    
    
    public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getUser_unique_id() {
		return user_unique_id;
	}
	public void setUser_unique_id(String user_unique_id) {
		this.user_unique_id = user_unique_id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getModified_by() {
		return modified_by;
	}
	public void setModified_by(Integer modified_by) {
		this.modified_by = modified_by;
	}

	public Timestamp getModified_at() {
		return modified_at;
	}
	public void setModified_at(Timestamp modified_at) {
		this.modified_at = modified_at;
	}
}
