package efrei.m1.aiws.rest;

import efrei.m1.aiws.model.requests.JSONUsersPostRequest;
import efrei.m1.aiws.model.requests.JSONUsersPostResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/users")
public class UsersResource {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public JSONUsersPostResponse createUser(JSONUsersPostRequest body) {
		return null;
	}


	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public JSONUsersPostResponse createUser(@FormParam("email") String email, @FormParam("password") String password, @FormParam("city") String city) {
		return null;
	}
}
