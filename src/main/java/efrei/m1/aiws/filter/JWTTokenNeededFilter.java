package efrei.m1.aiws.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

public class JWTTokenNeededFilter implements ContainerRequestFilter {
	@Override
	public void filter(ContainerRequestContext requestContext) {
		requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Needs authentication").build());
	}
}
