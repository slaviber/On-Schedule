package rest;

import internal.Group;
import internal.Participant;
import internal.Response;
import internal.User;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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
	
	@DELETE
	@Path("/{uid}")
	public void deleteGroup(@PathParam("uid") long uid) {
		final EntityManager em = factory.createEntityManager();
		final EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.remove(em.find(Group.class, uid));
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			em.close();
		} 
	}
	
	@POST
	@Path("/{uid}/Participant")
	public void addParticipant(@PathParam("uid") long groupID, Participant part) throws Exception{
		//TODO: only moderators should be able to add CHECKED participants
		EntityManager em;
		em = factory.createEntityManager();
		Group g;
		List<Participant> participants;
		try {
			g = em.find(Group.class, groupID);
			participants = g.getParticipants();
		} finally {
			em.close();
		}
		
		if(participants.contains(part))throw new Exception();
		participants.add(part);
		
		em = factory.createEntityManager();
		final EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.find(Group.class, groupID).setParticipants(participants);
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			em.close();
		} 
	}
	
	@DELETE
	@Path("/{uid}/Request/{userUID}")
	public void removeParticipationrequest(@PathParam("uid") long groupID, @PathParam("userUID") long userUID) throws Exception{
		//TODO: only moderators should be able to remove requests
		EntityManager em;
		em = factory.createEntityManager();
		Group g;
		List<Long> requesting;
		try {
			g = em.find(Group.class, groupID);
			requesting = g.getRequesting();
		} finally {
			em.close();
		}
		
		requesting.remove(userUID);
		
		em = factory.createEntityManager();
		final EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.find(Group.class, groupID).setRequesting(requesting);
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			em.close();
		} 
	}
	
	@POST
	@Path("/{uid}/Request")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addParticipationrequest(@Context SecurityContext security,@PathParam("uid") long groupID) throws Exception{
		EntityManager em;
 		Response r = new Response();
 		r.uid = 0;
 		r.code = Response.code_UNWANTED;
		em = factory.createEntityManager();
		User u;
		Group g;
		List<Long> requesting;
		List<Participant> participants;
		long userUID;
		long creator;
		try {
			u = em.createNamedQuery("getUserByUN", User.class).setParameter("UN", security.getUserPrincipal().getName()).getSingleResult();
			g = em.find(Group.class, groupID);
			requesting = g.getRequesting();
			participants = g.getParticipants();
			userUID = u.getUid();
			creator = g.getCreator();
		} finally {
			em.close();
		}
		
		for(long i : requesting)if(i == userUID)throw new Exception();
		for(Participant p : participants)if(p.getUid() == userUID)throw new Exception();
		if(creator == userUID)throw new Exception();
		requesting.add(userUID);
		
		em = factory.createEntityManager();
		final EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.find(Group.class, groupID).setRequesting(requesting);
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			em.close();
		} 
 		r.uid = groupID;
 		r.code = Response.code_OK;
		return r;
 		
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
