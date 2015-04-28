package rest;


import java.util.ArrayList;
import java.util.List;

import internal.Schedule;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("Schedules")
public class ScheduleRest {
	static EntityManagerFactory factory = GroupRest.factory;
	@GET
	@Path("/{uid}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Schedule> getSchedule(@PathParam("uid") long uid) {
		final EntityManager em = factory.createEntityManager();
		try {
			List<Schedule> all = em.createNamedQuery("allSchedules", Schedule.class).getResultList();
			List<Schedule> generated = new ArrayList<>();
			for(Schedule s : all){
				if(s.getGroupUid() == uid){
					generated.add(s);
				}
			}
			return generated;
		} finally {
			em.close();
		}
		
	}

}
