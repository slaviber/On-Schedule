package internal;

import javax.persistence.Embeddable;

@Embeddable
public class Participant{
	private long uid;
	private boolean canModerate;
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public boolean isCanModerate() {
		return canModerate;
	}
	public void setCanModerate(boolean canModerate) {
		this.canModerate = canModerate;
	}
}
