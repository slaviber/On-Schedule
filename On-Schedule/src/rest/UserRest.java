package rest;

import java.io.IOException;
import java.util.List;

import internal.Response;
import internal.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

@Path("Users")
public class UserRest {
	static EntityManagerFactory factory = GroupRest.factory;
	
	public UserRest(){
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
	@Path("/[{ids}]")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsers(@PathParam("ids") List<Long> ids){
		final EntityManager em = factory.createEntityManager();
		try {
			return em.createNamedQuery("someUsers", User.class).setParameter("ids", ids).getResultList();
		} finally {
			em.close();
		}
	}
	
	@GET
	@Path("/AccountDummy")
	@Produces(MediaType.TEXT_HTML)
	public String getLoggedInUser(@Context SecurityContext security){
		String username = security.getUserPrincipal().getName();
		final EntityManager em = factory.createEntityManager();
		try {
			return "{\"user\": " + em.createNamedQuery("getUserByUN", User.class).setParameter("UN", username).getSingleResult().getUid() + "}";
		} finally {
			em.close();
		}
	}
	
	@DELETE
	@Path("/AccountDummy")
	@Consumes(MediaType.APPLICATION_JSON)
	public void InvalidateLoggedInUser(@Context HttpServletRequest request, @Context HttpServletResponse response) throws IOException{
		request.getSession(false).invalidate();
		request.getSession(true);
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(User user){
		if(user.getUsername().equals("guest"))user.setRoleGroup("guest");
		else user.setRoleGroup("registered");
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