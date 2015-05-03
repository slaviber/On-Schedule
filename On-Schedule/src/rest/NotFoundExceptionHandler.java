package rest;

import java.util.List;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionHandler implements ExceptionMapper<NotFoundException>{

    @Context
    private HttpHeaders headers;

    public Response toResponse(NotFoundException ex){
        return Response.status(404).entity("deiba").type( getAcceptType()).build();
    }

    private String getAcceptType(){
         List<MediaType> accepts = headers.getAcceptableMediaTypes();
         if (accepts!=null && accepts.size() > 0) {
        	 return MediaType.APPLICATION_JSON;
         }else {
             return MediaType.APPLICATION_JSON;
         }
    }
}