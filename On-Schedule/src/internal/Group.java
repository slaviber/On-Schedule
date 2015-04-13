package internal;

import java.util.List;

public class Group {
	private String name;
	private long creator;
	private String description;
	private boolean isPrivate;
	private List<Participant> participants;
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
