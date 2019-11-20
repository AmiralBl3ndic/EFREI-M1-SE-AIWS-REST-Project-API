package efrei.m1.aiws.rest;

import efrei.m1.aiws.model.requests.JSONAuthPostRequest;
import efrei.m1.aiws.service.JWTService;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/auth")
public class AuthEndpoint {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String authenticate(JSONAuthPostRequest body, @Context final HttpServletResponse response) {
		// TODO: authenticate user from body better than following
		if (body.getEmail().equals("admin@mail.com") && body.getPassword().equals("admin")) {
			return JWTService.createToken(body.getEmail());
		} else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return "Wrong email/password";
		}
	}
}
