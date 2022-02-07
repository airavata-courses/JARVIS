package com.ads.assignment1.dbaccess.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import com.ads.assignment1.dbaccess.Pojos.UserSearchHistory;
import javax.persistence.ParameterMode;

@Entity
@SqlResultSetMapping(
    name = "UserSearchRes", classes = {
        @ConstructorResult(targetClass = UserSearchHistory.class,
        columns = {
            @ColumnResult(name = "place_name", type = String.class),
            @ColumnResult(name ="data_link", type = String.class),
            @ColumnResult(name ="searched_time", type = String.class),
            @ColumnResult(name ="location_searched_at", type = String.class)
        }
        )
    }
)

@NamedStoredProcedureQueries({
@NamedStoredProcedureQuery(name = "myapp.GetUserSearchHistory", procedureName = "myapp.GetUserSearchHistory",resultSetMappings = {"UserSearchRes"},
parameters = {
    @StoredProcedureParameter(mode = ParameterMode.IN, name="unique_id", type = String.class)

})
})

@Table(name = "history_master", schema = "myapp")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "serial")
	private long history_id;
	
	@Column(name = "user_id")
	private long user_id;
	
	@Column(name = "place_id")
	private long place_id;
	
	@Column (name = "data_link")
	private String data_link;
	
	@Column (name = "status")
	private Integer status;
	
	@Column (name = "created_by")
	private Integer created_by;
	
	@Column (name = "searched_time")
	private String searched_time;
	
	@Column (name = "location_searched_at")
	private String location_searched_at;

	
	public long getHistory_id() {
		return history_id;
	}

	public void setHistory_id(long history_id) {
		this.history_id = history_id;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public long getPlace_id() {
		return place_id;
	}

	public void setPlace_id(long place_id) {
		this.place_id = place_id;
	}

	public String getData_link() {
		return data_link;
	}

	public void setData_link(String data_link) {
		this.data_link = data_link;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public String getlocation_searched_at() {
		return location_searched_at;
	}

	public void setLocation_searched_at(String location_searched_at) {
		this.location_searched_at = location_searched_at;
	}
}