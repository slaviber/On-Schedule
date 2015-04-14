package internal;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2015-04-14T05:27:33.262+0300")
@StaticMetamodel(Group.class)
public class Group_ {
	public static volatile SingularAttribute<Group, String> name;
	public static volatile SingularAttribute<Group, Long> creator;
	public static volatile SingularAttribute<Group, String> description;
	public static volatile SingularAttribute<Group, Boolean> isPrivate;
	public static volatile ListAttribute<Group, Participant> participants;
	public static volatile SingularAttribute<Group, Long> uid;
}
