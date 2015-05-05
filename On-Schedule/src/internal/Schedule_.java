package internal;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-05-05T06:14:17.090+0300")
@StaticMetamodel(Schedule.class)
public class Schedule_ {
	public static volatile SingularAttribute<Schedule, Long> uid;
	public static volatile SingularAttribute<Schedule, String> creationDate;
	public static volatile ListAttribute<Schedule, Integer> associatedTaskIds;
	public static volatile SingularAttribute<Schedule, String> description;
	public static volatile SingularAttribute<Schedule, Long> group_uid;
	public static volatile SingularAttribute<Schedule, Boolean> isFinalized;
}
