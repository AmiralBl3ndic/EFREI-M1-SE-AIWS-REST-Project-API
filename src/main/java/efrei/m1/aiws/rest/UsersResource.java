package efrei.m1.aiws.rest;

import static efrei.m1.aiws.utils.Constants.*;

import efrei.m1.aiws.dao.UserDAOImpl;
import efrei.m1.aiws.model.User;
import efrei.m1.aiws.model.requests.JSONUsersPostRequest;
import efrei.m1.aiws.model.requests.JSONUsersPostResponse;

import efrei.m1.aiws.service.JWTService;
import lombok.Setter;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
public class UsersResource {

	@Setter
	private static UserDAOImpl userDAO;

	/**
	 * Processes a POST request ({@link User} database records creation)
	 * @param email Email of the {@link User} to create
	 * @param password Password of the {@link User} to create
	 * @param city City of the {@link User} to create
	 * @return HTTP {@link Response} to send back to the client
	 */
	private Response processPOST(String email, String password, String city) {
		JSONUsersPostResponse res = new JSONUsersPostResponse();

		// Check if any field is empty
		if (email.isEmpty() || password.isEmpty() || city.isEmpty()) {
			res.setError(USERS_ERROR_EMPTY_FIELDS);
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity(res).build();
		}

		// Create database record (which automatically sets the generated id)
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setCity(city);
		UsersResource.userDAO.create(user);

		// If database record creation failed (no dbId set for user object)
		if (user.getDbId() == null) {
			res.setError(USERS_ERROR_CANNOT_CREATE);
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity(res).build();
		}


		// Obtain JWT token (so that the user does not need to authenticate after creating an account)
		final String jwtToken = JWTService.createToken(user.getDbId(), user.getEmail());

		// Build response object
		res.setId(user.getDbId());
		res.setToken(jwtToken);

		return Response.status(Response.Status.CREATED).entity(res).build();
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
