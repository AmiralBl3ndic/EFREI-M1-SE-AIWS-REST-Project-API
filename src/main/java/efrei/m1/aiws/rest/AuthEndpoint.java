package efrei.m1.aiws.rest;

import efrei.m1.aiws.model.User;
import efrei.m1.aiws.model.requests.JSONAuthPostRequest;
import efrei.m1.aiws.model.requests.JSONAuthPostResponse;
import efrei.m1.aiws.service.AuthenticationService;
import efrei.m1.aiws.service.JWTService;

import static efrei.m1.aiws.utils.Constants.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
public class AuthEndpoint {
	/**
	 * Processes an incoming POST request for authentication purposes
	 * @param email Email of the {@link User} to authenticate
	 * @param password Password of the {@link User} to authenticate
	 * @return HTTP {@link Response} to send back to the client
	 */
	private Response processPOST(final String email, final String password) {
		User authenticatedUser = AuthenticationService.authenticateUser(email, password);

		JSONAuthPostResponse res = new JSONAuthPostResponse();

		if (authenticatedUser != null) {
			res.setToken(JWTService.createToken(authenticatedUser.getDbId(), authenticatedUser.getEmail()));
			return Response.ok().entity(res).build();
		}

		res.setError(AUTH_ERROR_WRONG_CREDENTIALS);
		return Response.status(Response.Status.UNAUTHORIZED).entity(res).build();
	}

	/**
	 * Processes POST requests made with a {@code "Content-Type": "application/json"} header
	 * @param body JSON object containing "email" and "password" keys
	 * @return HTTP {@link Response} in the form of a {@link JSONAuthPostResponse} JSON object
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticate(JSONAuthPostRequest body) {
		return this.processPOST(body.getEmail(), body.getPassword());
	}

	/**
	 * Processes POST requests made with a {@code "Content-Type": "form/x-www-form-urlencoded"} header
	 * @param email Email of the {@link User} to authenticate
	 * @param password Password of the {@link User} to authenticate
	 * @return HTTP {@link Response} in the form of a {@link JSONAuthPostResponse} JSON object
	 */
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticate(@FormParam("email") final String email, @FormParam("password") final String password) {
		return this.processPOST(email, password);
	}
}
