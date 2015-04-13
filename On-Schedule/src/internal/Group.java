package internal;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity(name="Groups")
@NamedQueries({
	@NamedQuery(name = "allGroups", 
		query = "SELECT g from Groups g")
})
public class Group {
	@Column(nullable=false)
	private String name;
	@Column(nullable=false)
	private long creator;
	@Column(nullable=false)
	private String description;
	@Column(nullable=false)
	private boolean isPrivate;
	
	  @ElementCollection(fetch=FetchType.EAGER)
	  @CollectionTable(
	        name="Participant"
	  )
	  private List<Participant> participants;
	
	@Id
	@GeneratedValue
	private long uid;
	
	private static long last_uid = 0;
	
	
	public long getID(){
		return last_uid++;
	}
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean getIsPrivate() {
		return isPrivate;
	}
	public void setIsPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getCreator() {
		return creator;
	}
	public void setCreator(long creator) {
		this.creator = creator;
	}
	public List<Participant> getParticipants() {
		return participants;
	}
	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}
}
