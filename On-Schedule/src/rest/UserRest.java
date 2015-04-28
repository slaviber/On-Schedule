package rest;

import java.util.ArrayList;
import java.util.List;

import internal.Group;
import internal.Response;
import internal.Schedule;
import internal.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("Users")
public class UserRest {
	static EntityManagerFactory factory = GroupRest.factory;
	
	@GET
	@Path("/[{ids}]")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsers(@PathParam("ids") List<Long> ids){
		//System.out.println(lg.isEmpty());
		//return lg;

		final EntityManager em = factory.createEntityManager();
		try {
			//return em.createNamedQuery("someUsers", User.class).setParameter("ids", ids).getResultList();
			
			//should be fixed; due to wrong java version
			List<User> gathered = new ArrayList<User>();
			for(Long id : ids){
				gathered.add(em.find(User.class, id));
			}
			return gathered;
		} finally {
			em.close();
		}
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(User user){
		user.setRoleGroup("registered");
		EntityManager em = factory.createEntityManager();
		final EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(user);
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			em.close();
		} 
 		Response r = new Response();
 		r.uid = user.getUid();
 		r.code = Response.code_OK;
		return r;
	}
	
}