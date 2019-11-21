package efrei.m1.aiws.rest;

import efrei.m1.aiws.model.requests.JSONAuthPostRequest;
import efrei.m1.aiws.model.requests.JSONAuthPostResponse;
import efrei.m1.aiws.service.JWTService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
public class AuthEndpoint {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticate(JSONAuthPostRequest body) {
		JSONAuthPostResponse res = new JSONAuthPostResponse();

		if (body.getEmail().equals("admin@mail.com") && body.getPassword().equals("admin")) {
			res.setToken(JWTService.createToken(body.getEmail()));
			return Response.status(Response.Status.OK).entity(res).build();
		}

		res.setError("Wrong email/password");
		return Response.status(Response.Status.UNAUTHORIZED).entity(res).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticate(@FormParam("email") final String email, @FormParam("password") final String password) {
		JSONAuthPostResponse res = new JSONAuthPostResponse();

		if (email.equals("admin@mail.com") && password.equals("admin")) {
			res.setToken(JWTService.createToken(email));
			return Response.status(Response.Status.OK).entity(res).build();
		}

		res.setError("Wrong email/password");
		return Response.status(Response.Status.UNAUTHORIZED).entity(res).build();
	}
}
