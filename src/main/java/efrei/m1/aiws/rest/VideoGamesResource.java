package efrei.m1.aiws.rest;

import efrei.m1.aiws.dao.VideoGameDAOImpl;
import lombok.Setter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/video-games")
public class VideoGamesResource {

	@Setter
	private static VideoGameDAOImpl videoGameDAO;

	/**
	 * Get the list of all video-games records in the database
	 * @return List of all video-games records in the database
	 */
	@GET
	public Response getVideoGames() {
		return Response.status(Response.Status.NOT_IMPLEMENTED).build();
	}
}
