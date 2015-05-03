package rest;

import internal.Group;
import internal.Response;
import internal.User;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

@Path("Groups")
public class GroupRest {
	
	public static EntityManagerFactory factory = null;
	
	public GroupRest(){
		if(factory == null){
			try {
				Class.forName("org.apache.derby.jdbc.ClientDriver");
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException("No driver", e);
			}
			factory = Persistence.createEntityManagerFactory("On-Schedule");
		}
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Group> getGroups(/*@Context final HttpServletResponse response*/) throws IOException{
		final EntityManager em = factory.createEntityManager();
		//response.setStatus(javax.ws.rs.core.Response.Status.ACCEPTED.getStatusCode());
		//response.flushBuffer();
		try {
			return em.createNamedQuery("allGroups", Group.class).getResultList();
		} finally {
			em.close();
		}
	}
	
	@GET
	@Path("/{uid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Group getGroup(@PathParam("uid") long uid) {
		final EntityManager em = factory.createEntityManager();
		try {
			return em.find(Group.class, uid);
		} finally {
				em.close();
		}
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createGroup(@Context SecurityContext security, Group group){
		EntityManager em;
 		Response r = new Response();
 		r.uid = 0;
 		r.code = Response.code_UNAUTHORIZED;
 		if(security.getUserPrincipal() == null)return r;
		
		em = factory.createEntityManager();
		User u;
		try {
			u = em.createNamedQuery("getUserByUN", User.class).setParameter("UN", security.getUserPrincipal().getName()).getSingleResult();
		} finally {
			em.close();
		}
 		group.setCreator(u.getUid());

		em = factory.createEntityManager();
		final EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(group);
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			em.close();
		} 
 		r.uid = group.getUid();
 		r.code = Response.code_OK;
		return r;
 		
	}
}
