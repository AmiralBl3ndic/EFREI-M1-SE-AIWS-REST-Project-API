package efrei.m1.aiws.rest.filter;

import efrei.m1.aiws.rest.filter.annotations.JWTTokenNeeded;
import efrei.m1.aiws.service.JWTService;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
@JWTTokenNeeded
@Priority(Priorities.AUTHENTICATION)
public class JWTTokenNeededFilter implements ContainerRequestFilter {
	@Override
	public void filter(ContainerRequestContext requestContext) {
		Response needsAuthentication = Response.status(Response.Status.UNAUTHORIZED).entity("Needs authentication").build();

		// Gathering JWT Token from Authorization HTTP header
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
			requestContext.abortWith(needsAuthentication);
			return;
		}

		String token = authorizationHeader.substring("Bearer".length()).trim();

		// Verify if token is empty or not legit (wrong signature), if so, block the request
		if (token.isEmpty() || !JWTService.isTokenLegit(token)) {
			requestContext.abortWith(needsAuthentication);
		}
	}
}
