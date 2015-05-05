package internal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity(name="Tasks")
@NamedQueries({
	@NamedQuery(name = "allTasks", 
	query = "SELECT t from Tasks t")
})
public class Task {
	//derivedFrom, beginDate, plannedEndDate, description, endDate, taskOwner, uid
	@Id
	@GeneratedValue
	private long uid;
	@Column(nullable=false)
	private long derivedFrom;
	@Column(nullable=false)
	private long taskOwner;
	private String beginDate;
	private String plannedEndDate;
	private String description;
	private String endDate;
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public long getDerivedFrom() {
		return derivedFrom;
	}
	public void setDerivedFrom(long derivedFrom) {
		this.derivedFrom = derivedFrom;
	}
	public long getTaskOwner() {
		return taskOwner;
	}
	public void setTaskOwner(long taskOwner) {
		this.taskOwner = taskOwner;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getPlannedEndDate() {
		return plannedEndDate;
	}
	public void setPlannedEndDate(String plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
