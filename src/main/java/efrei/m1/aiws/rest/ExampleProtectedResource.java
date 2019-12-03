package efrei.m1.aiws.rest;

import efrei.m1.aiws.rest.filter.annotations.JWTTokenNeeded;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/example/protected")
public class ExampleProtectedResource {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@JWTTokenNeeded
	public Response getResource() {
		return Response.ok("Passed").build();
	}
}
