package efrei.m1.aiws.rest;

import efrei.m1.aiws.model.User;
import efrei.m1.aiws.model.requests.JSONUsersPostRequest;
import efrei.m1.aiws.model.requests.JSONUsersPostResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
public class UsersResource {
	/**
	 * Processes a POST request ({@link User} database records creation)
	 * @param email Email of the {@link User} to create
	 * @param password Password of the {@link User} to create
	 * @param city City of the {@link User} to create
	 * @return HTTP {@link Response} to send back to the client
	 */
	private Response processPOST(String email, String password, String city) {
		JSONUsersPostResponse res = new JSONUsersPostResponse();

		return Response.status(Response.Status.NOT_IMPLEMENTED).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(JSONUsersPostRequest body) {
		return this.processPOST(body.getEmail(), body.getPassword(), body.getCity());
	}


	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(@FormParam("email") String email, @FormParam("password") String password, @FormParam("city") String city) {
		return this.processPOST(email, password, city);
	}
}
