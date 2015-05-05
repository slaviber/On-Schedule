package rest;


import java.util.ArrayList;
import java.util.List;

import internal.Response;
import internal.Schedule;
import internal.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
	public List<Schedule> getGroupSchedule(@PathParam("uid") long uid) {
		final EntityManager em = factory.createEntityManager();
		try {
			return em.createNamedQuery("groupSchedules", Schedule.class).setParameter("GID", uid).getResultList();
		} finally {
			em.close();
		}
		
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addSchedule(Schedule sched) {
		final EntityManager em = factory.createEntityManager();
		final EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(sched);
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			em.close();
		} 
		Response r = new Response();
		r.code = Response.code_OK;
		r.uid = sched.getUid();
		return r;
	}

}
