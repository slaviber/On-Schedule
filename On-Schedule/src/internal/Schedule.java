package internal;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity(name="Schedules")
@NamedQueries({
	@NamedQuery(name = "allSchedules", 
	query = "SELECT s from Schedules s")
})
public class Schedule {
	
	@Id
	@GeneratedValue
	private long uid;
	@Column(nullable=false)
	private String creationDate;
	@Column(nullable=false)
	private boolean isFinalized;
	@Column(nullable=false)
	private long groupUid;
	@ElementCollection(fetch=FetchType.EAGER)
	List<Integer> associatedTaskIds;
	@Column(nullable=false)
	private String description;
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public boolean isFinalized() {
		return isFinalized;
	}
	public void setFinalized(boolean isFinalized) {
		this.isFinalized = isFinalized;
	}
	public long getGroupUid() {
		return groupUid;
	}
	public void setGroupUid(long groupUid) {
		this.groupUid = groupUid;
	}
	public List<Integer> getAssociatedTaskIds() {
		return associatedTaskIds;
	}
	public void setAssociatedTaskIds(List<Integer> associatedTaskIds) {
		this.associatedTaskIds = associatedTaskIds;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
