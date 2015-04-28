package internal;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-04-26T18:19:40.266+0300")
@StaticMetamodel(Schedule.class)
public class Schedule_ {
	public static volatile SingularAttribute<Schedule, Long> uid;
	public static volatile SingularAttribute<Schedule, String> creationDate;
	public static volatile SingularAttribute<Schedule, Boolean> isFinalized;
	public static volatile SingularAttribute<Schedule, Long> groupUid;
	public static volatile ListAttribute<Schedule, Integer> associatedTaskIds;
	public static volatile SingularAttribute<Schedule, String> description;
}
