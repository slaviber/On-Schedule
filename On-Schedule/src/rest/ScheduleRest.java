package rest;


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
	public Schedule getSchedule(@PathParam("uid") long uid) {

		
		final EntityManager em = factory.createEntityManager();
		try {
			em.createNamedQuery("allSchedules", Schedule.class).getResultList();
			return null;
		} finally {
			em.close();
		}
		
	}

}
