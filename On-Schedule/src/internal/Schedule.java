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
	query = "SELECT s from Schedules s"),
	@NamedQuery(name = "groupSchedules", 
	query = "SELECT s from Schedules s where s.group_uid = (:GID)")
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
	private long group_uid;
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
	public boolean getIsFinalized() {
		return isFinalized;
	}
	public void setIsFinalized(boolean isFinalized) {
		this.isFinalized = isFinalized;
	}
	public long getGroup_uid() {
		return group_uid;
	}
	public void setGroup_uid(long groupUid) {
		this.group_uid = groupUid;
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
