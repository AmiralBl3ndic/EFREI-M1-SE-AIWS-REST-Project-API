package efrei.m1.aiws.rest;

import efrei.m1.aiws.model.VideoGame;

import efrei.m1.aiws.dao.VideoGameDAOImpl;
import efrei.m1.aiws.rest.filter.annotations.JWTTokenNeeded;
import lombok.Setter;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/video-games")
public class VideoGamesResource {

	@Setter
	private static VideoGameDAOImpl videoGameDAO;

	///region GET requests
	/**
	 * Get the list of all video-games records in the database
	 * @return List of all video-games records in the database
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVideoGames() {
		return Response.status(Response.Status.NOT_IMPLEMENTED).build();
	}

	/**
	 * Get the details of the video-game with database id {@code id}
	 * @param id Database id of the video-game to get the details of
	 * @return Details of a specific video-game if it has a record in the database, {@code 404 NOT_FOUND} otherwise
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVideoGameDetails(@PathParam("id") String id) {
		return Response.status(Response.Status.NOT_IMPLEMENTED).build();
	}

	/**
	 * Get the comments of the video-game with database id {@code id}
	 * @param id Database id of the video-game to get the comments of
	 * @return List tof comments of a specific video-game record if it has a record in the database, {@code 404 NOT_FOUND} otherwise
	 */
	@GET
	@Path("{id}/comments")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVideoGameComments(@PathParam("id") String id) {
		return Response.status(Response.Status.NOT_IMPLEMENTED).build();
	}
	///endregion


	///region POST requests
	/**
	 * Handle the {@code POST} requests made to the /video-games endpoint
	 * @return HTTP Response to send to the user
	 */
	private Response handlePostVideoGames(/* TODO: define and insert parameters */) {
		return Response.status(Response.Status.NOT_IMPLEMENTED).build();
	}

	/**
	 * Handle the {@code POST} requests made to the /video-games/{id}/comments endpoint
	 * @return HTTP Response to send to the user
	 */
	private Response handlePostVideoGameComment(/* TODO: define and insert parameters */) {
		return Response.status(Response.Status.NOT_IMPLEMENTED).build();
	}


	/**
	 * Creates a {@link VideoGame} record in the database from a {@code 'application/json'} content type
	 * @param body JSON object containing the data needed to create a {@link VideoGame} record
	 * @return HTTP Response to send to the user
	 */
	@POST
	@JWTTokenNeeded
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postVideoGame(
		/* TODO: create a simple class to hold the needed request parameters */
		Object body
	) {
		return this.handlePostVideoGames();
	}

	/**
	 * Creates a {@link VideoGame} record in the database from a {@code 'application/x-www-form-urlencoded'} content type
	 * @return HTTP Response to send to the user
	 */
	@POST
	@JWTTokenNeeded
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postVideoGame(
		/* TODO: define all the needed parameters */
	) {
		return this.handlePostVideoGames();
	}

	/**
	 * Creates a {@link VideoGame} {@code comment} record in the database from a {@code 'application/json'} content type
	 * @param body JSON object containing the data needed to create a {@link VideoGame} {@code comment} record
	 * @return HTTP Response to send to the user
	 */
	@POST
	@Path("{id}/comments")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postVideoGameComment(
		/* TODO: create a simple class to hold the needed request parameters */
		Object body
	) {
		return this.handlePostVideoGameComment();
	}

	/**
	 * Creates a {@link VideoGame} {@code comment} record in the database from a {@code 'application/json'} content typ
	 * @return HTTP Response to send to the user
	 */
	@POST
	@Path("{id}/comments")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postVideoGameComment(
		/* TODO: define all the needed parameters */
	) {
		return this.handlePostVideoGameComment();
	}
	///endregion
}
