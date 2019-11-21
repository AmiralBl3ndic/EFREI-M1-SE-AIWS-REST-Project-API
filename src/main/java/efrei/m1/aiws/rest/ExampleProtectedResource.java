package efrei.m1.aiws.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/example/protected")
public class ExampleProtectedResource {
	@GET
	public Response getResource() {
		return Response.status(Response.Status.OK).build();
	}
}
