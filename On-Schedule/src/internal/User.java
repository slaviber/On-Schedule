package internal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@Entity(name="Users")
@NamedQueries({
	@NamedQuery(name = "someUsers", 
		query = "SELECT u from Users u where u.uid in (:ids)"),
	@NamedQuery(name = "getUserById", 
	query = "SELECT u from Users u where u.uid like (:id)"),
	@NamedQuery(name = "allUsers", 
	query = "SELECT u from Users u"),
	@NamedQuery(name = "getUserByUN", 
	query = "SELECT u from Users u where u.username like (:UN)")
})
public class User {
	
	@Id
	@GeneratedValue
	private long uid;
	
	@Column(nullable=false)
	private String name;
	
	@XmlTransient
	@Column(nullable=false, unique=true)
	private String username;
	
	@XmlTransient
	@Column(nullable=false)
	private String password;
	
	@XmlTransient
	@Column
	private String roleGroup;
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRoleGroup() {
		return roleGroup;
	}
	public void setRoleGroup(String roleGroup) {
		this.roleGroup = roleGroup;
	}
}
