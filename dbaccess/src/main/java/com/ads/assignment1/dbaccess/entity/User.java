package com.ads.assignment1.dbaccess.entity;



import java.sql.Timestamp;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import com.ads.assignment1.dbaccess.Pojos.UserSearchHistory;
import com.ads.assignment1.dbaccess.Pojos.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SqlResultSetMapping(
	    name = "UserStatusRes", classes = {
	        @ConstructorResult(targetClass = UserStatus.class,
	        columns = {
	            @ColumnResult(name = "status", type = String.class),
	            @ColumnResult(name = "message", type = String.class)
	        }
	        )
	    }
	)
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "myapp.InsertUser", procedureName = "myapp.InsertUser", resultSetMappings = {"UserStatusRes"},
			parameters = {
			    @StoredProcedureParameter(mode = ParameterMode.IN, name="unique_id", type = String.class),
			    @StoredProcedureParameter(mode = ParameterMode.IN, name="modified_at", type = String.class),
			    //@StoredProcedureParameter(mode = ParameterMode.OUT, name="insertuser", type = String.class)
			})
})

@Table(name = "user_master", schema = "myapp")
public class User {

    @Id
    @GeneratedValue
    // user_master table column
    private int user_id;
    private String user_unique_id;
    private Integer status;
	private Integer modified_by;
    private String modified_at;
    
    // Setter and getter of each column
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

	public String getModified_at() {
		return modified_at;
	}
	public void setModified_at(String modified_at) {
		this.modified_at = modified_at;
	}
}
