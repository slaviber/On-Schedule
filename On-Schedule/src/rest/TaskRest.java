package rest;

import internal.Task;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("Tasks")
public class TaskRest {
	static EntityManagerFactory factory = GroupRest.factory;
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Task> getAllTasks() {
		final EntityManager em = factory.createEntityManager();
		try {
			return em.createNamedQuery("allTasks", Task.class).getResultList();
		} finally {
			em.close();
		}
		
	}
}
