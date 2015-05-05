package internal;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-05-05T07:05:33.337+0300")
@StaticMetamodel(Task.class)
public class Task_ {
	public static volatile SingularAttribute<Task, Long> uid;
	public static volatile SingularAttribute<Task, String> description;
	public static volatile SingularAttribute<Task, Long> derivedFrom;
	public static volatile SingularAttribute<Task, String> beginDate;
	public static volatile SingularAttribute<Task, String> plannedEndDate;
	public static volatile SingularAttribute<Task, String> endDate;
	public static volatile SingularAttribute<Task, Long> taskOwner;
}
