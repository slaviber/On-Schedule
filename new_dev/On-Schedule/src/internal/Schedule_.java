package internal;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-04-14T06:08:56.950+0300")
@StaticMetamodel(Schedule.class)
public class Schedule_ {
	public static volatile SingularAttribute<Schedule, Long> uid;
	public static volatile SingularAttribute<Schedule, String> creationDate;
	public static volatile SingularAttribute<Schedule, Boolean> isFinalized;
	public static volatile ListAttribute<Schedule, Integer> associatedTaskIds;
	public static volatile SingularAttribute<Schedule, String> description;
	public static volatile SingularAttribute<Schedule, Long> groupUid;
}
