package rest;

import internal.Group;
import internal.Participant;
import internal.Response;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("Groups")
public class GroupRest {
	
	List<Group> lg = new ArrayList();
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Group> getGroups(){
		System.out.println(lg.isEmpty());
		return lg;
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createGroup(Group group){
 		// TODO set author by user session
 		group.setCreator(1);
 		group.setUid(group.getID());
 		
 		lg.add(group);
 		System.out.println(lg.isEmpty());
 		
 		Response r = new Response();
 		r.uid = group.getUid();
 		r.code = Response.code_OK;
 		return r;
 		
	}
}
