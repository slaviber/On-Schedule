package rest;

import internal.Group;
import internal.Response;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("Groups")
public class GroupRest {
	
	static EntityManagerFactory factory = null;
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Group> getGroups(){
		//System.out.println(lg.isEmpty());
		//return lg;
		
		if(factory == null){
			try {
				Class.forName("org.apache.derby.jdbc.ClientDriver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				throw new IllegalStateException("No driver", e);
			}
			factory = Persistence.createEntityManagerFactory("On-Schedule");
		}

		final EntityManager em = factory.createEntityManager();
		try {
			return em.createNamedQuery("allGroups", Group.class).getResultList();
		} finally {
			em.close();
		}
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createGroup(Group group){
		
		
 		// TODO set author by user session
 		group.setCreator(1);


 		
		EntityManager em = factory.createEntityManager();
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
		
 		Response r = new Response();
 		r.uid = group.getUid();
 		r.code = Response.code_OK;
		return r;
 		
	}
}
