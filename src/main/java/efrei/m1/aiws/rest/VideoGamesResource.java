package efrei.m1.aiws.rest;

import efrei.m1.aiws.dao.VideoGameDAOImpl;
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
}
