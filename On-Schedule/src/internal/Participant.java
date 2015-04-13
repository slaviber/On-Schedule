package internal;

public class Participant<L,R> {
	private long uid;
    private boolean canModerate;
    public Participant(long uid, boolean canModerate){
        this.uid = uid;
        this.canModerate = canModerate;
    }
    public boolean getCanModerate(){ 
    	return canModerate; 
    }
    public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public void setCanModerate(boolean canModerate) {
		this.canModerate = canModerate;
	}
}
