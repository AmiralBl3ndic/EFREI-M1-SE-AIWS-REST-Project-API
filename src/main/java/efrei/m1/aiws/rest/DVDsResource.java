package efrei.m1.aiws.rest;

import efrei.m1.aiws.dao.UserDAOImpl;
import efrei.m1.aiws.dao.DVDDAOImpl;
import efrei.m1.aiws.model.Comment;
import efrei.m1.aiws.model.User;
import efrei.m1.aiws.model.DVD;
import efrei.m1.aiws.rest.filter.annotations.JWTTokenNeeded;
import efrei.m1.aiws.service.JWTService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static efrei.m1.aiws.utils.Constants.*;

/**
 * Simple class to represent the incoming HTTP Request JSON Object
 */
@Data @NoArgsConstructor
class DVDResourceRequest {
    private String userId;
    private String ageLimit;
    private String duration;
    private String title;
    private String type;
    private String description;
    private String editor;
    private String audio;
    private String releaseDate;
}

/**
 * Simple class to represent the outgoing HTTP Response JSON Object
 */
@Data @NoArgsConstructor
class DVDResourceResponse {
    private String error = "";

    private List<DVD> items = new ArrayList<>();

    void addItem(DVD item) {
        this.items.add(item);
    }
}

@NoArgsConstructor @Data
class DVDRessourceCommentRequest{
    String comment;
}

class DVDRessourceCommentResponse{
    private String error = "";
    private List<Comment> items = new ArrayList<>();

    void addItem(Comment item){
        this.items.add(item);
    }
}

@Path("/dvds")
public class DVDsResource {

    @Setter
    private static DVDDAOImpl dvdDAO;

    @Setter
    private static UserDAOImpl userDAO;

    /**
     * Get the user id associated with the passed in Authorization HTTP header (JWT token)
     * @param authorizationHeader {@code Authorization} HTTP header value
     * @return User id associated with the passed in Authorization HTTP header (JWT token)
     */
    private String getUserIdFromAuthorizationHeader(String authorizationHeader) {
        final String jwtToken = JWTService.extractTokenFromHeader(authorizationHeader);
        User clientUserRecord = JWTService.getUserFromToken(jwtToken);

        // The following check should never be true
        if (clientUserRecord == null || clientUserRecord.getDbId() == null) {
            return "";
        }

        return clientUserRecord.getDbId();
    }

    /**
     * Applies the requests URL parameters (that we call filters)
     * @param dvds List of {@link DVD}s to perform the filtering on
     * @param limitParam Maximum number of items to return
     * @param startParam Minimum (inclusive) id of the records to return
     * @param keywordsParam Keywords to look for (records below {@code FUZZY_SEARCH_MATCH_THRESHOLD} similarity will be excluded)
     * @param creatorParam Id of the user who created the record
     * @param cityParam City to look for records in
     * @return List of {@link DVD}s that meet all the filters requirements
     */
    private ArrayList<DVD> applyGetUrlParameters(
            ArrayList<DVD> dvds,
            String limitParam,
            String startParam,
            String keywordsParam,
            String creatorParam,
            String cityParam
    ) {
        // Handle "creator" url parameter
        if (creatorParam != null && !creatorParam.isEmpty()) {
            dvds.removeIf(dvd -> !dvd.getUserId().equals(creatorParam));
        }

        // Handle "keywords" url parameter
        if (keywordsParam != null && !keywordsParam.isEmpty()) {
            dvds.removeIf(dvd -> FuzzySearch.weightedRatio(dvd.getTitle(), keywordsParam) < FUZZY_SEARCH_MATCH_THRESHOLD);
        }

        // Handle "city" url parameter
        if (cityParam != null && !cityParam.isEmpty()) {
            dvds.removeIf(dvd -> {
                // Gather creator of dvd record
                User creator = userDAO.findBy(dvd.getUserId());
                if (creator == null) {
                    return true;
                }

                return !creator.getCity().equalsIgnoreCase(cityParam);
            });
        }

        // Handle "start" url parameter
        if (startParam != null && !startParam.isEmpty()) {
            int minId = Integer.parseInt(startParam);
            dvds.removeIf(dvd -> Integer.parseInt(dvd.getDvdId()) < minId);
        }

        // Handle "limit" url parameter
        if (limitParam != null && !limitParam.isEmpty()) {
            int maxRecords = Integer.parseInt(limitParam);
            dvds = new ArrayList<>(dvds.subList(0, maxRecords));
        }

        return dvds;
    }

    /**
     * Check whether an integer parameter is greater than 0 (so that it is usable)
     * @param parameter {@link String} value of the parameter to check
     * @return Whether an integer parameter is greater than 0 (so that it is usable)
     */
    private String isIntegerParameterValid(String parameter) {
        int paramValue;

        try {
            paramValue = Integer.parseInt(parameter);
        } catch (NumberFormatException e) {
            return DVDS_ILLEGAL_FILTER_TYPE_INT;
        }

        if (paramValue < 0) {
            return DVDS_ILLEGAL_FILTER_VALUE;
        }

        return "";
    }


    ///region GET requests
    /**
     * Get the list of all dvds records in the database
     * @return List of all dvds records in the database
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDVDs(
            @QueryParam("limit") String limitParam,
            @QueryParam("start") String startParam,
            @QueryParam("keywords") String keywordsParam,
            @QueryParam("creator") String creatorParam,
            @QueryParam("city") String cityParam
    ) {
        DVDResourceResponse res = new DVDResourceResponse();

        ArrayList<DVD> DVDs = (ArrayList<DVD>) dvdDAO.findAll();

        // Check if "start" url parameter can be used
        if (startParam != null && !startParam.isEmpty()) {
            res.setError(this.isIntegerParameterValid(startParam));
            if (!res.getError().equals("")) {
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity(res).build();
            }
        }

        // Check if "limit" url parameter can be used
        if (limitParam != null && !limitParam.isEmpty()) {
            res.setError(this.isIntegerParameterValid(limitParam));
            if (!res.getError().equals("")) {
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity(res).build();
            }
        }

        // Apply filters
        DVDs = this.applyGetUrlParameters(DVDs, limitParam, startParam, keywordsParam, creatorParam, cityParam);

        res.setItems(DVDs);
        return Response.ok().entity(res).build();
    }

    /**
     * Get the details of the dvd with database id {@code id}
     * @param DvdId Database id of the dvd to get the details of
     * @return Details of a specific dvd if it has a record in the database, {@code 404 NOT_FOUND} otherwise
     */

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDVDDetails(@PathParam("id") String DvdId) {
        DVDResourceResponse res = new DVDResourceResponse();
        DVD item = dvdDAO.findBy(DvdId);

        if (item != null) {
            res.addItem(item);
            return Response.ok().entity(res).build();
        } else {
            res.setError(DVDS_ERROR_NOT_FOUND);
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
    }

    /**
     * Get the comments of the dvd with database id {@code id}
     * @param DvdId Database id of the dvd to get the comments of
     * @return List tof comments of a specific dvd record if it has a record in the database, {@code 404 NOT_FOUND} otherwise
     */
    @GET
    @Path("{id}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDVDComments(@PathParam("id") String DvdId) {
        DVDResourceResponse res = new DVDResourceResponse();
        DVD item = dvdDAO.findBy(DvdId);

        if (item != null) {
            res.setError(DVDS_ERROR_NOT_FOUND);
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        } else {
            return Response.status(Response.Status.NOT_IMPLEMENTED).entity(res).build();
        }
    }
    ///endregion


    ///region POST requests
    /**
     * Handle the {@code POST} requests made to the /dvds endpoint
     * @param userId Database id of the user that creates the record
     * @return HTTP Response to send to the user
     */
    private Response handlePostDVDs(
            String userId,
            String ageLimit,
            String duration,
            String title,
            String type,
            String description,
            String editor,
            String audio,
            String releaseDate
    ) {
        DVDResourceResponse res = new DVDResourceResponse();
        DVD newRecord = new DVD();

        newRecord.setUserId(userId);
        newRecord.setAgeLimit(ageLimit);
        newRecord.setDuration(duration);
        newRecord.setTitle(title);
        newRecord.setType(type);
        newRecord.setDescription(description);
        newRecord.setEditor(editor);
        newRecord.setAudio(audio);
        newRecord.setReleaseDate(releaseDate);

        dvdDAO.create(newRecord);

        if (newRecord.getDvdId() == null) {  // Check if request failed
            res.setError(DVDS_ERROR_NOT_CREATED);
            return Response.status(Response.Status.NOT_MODIFIED).entity(res).build();
        } else {
            res.setError("");
            res.addItem(newRecord);
            return Response.status(Response.Status.CREATED).entity(res).build();
        }
    }

    /**
     * Handle the {@code POST} requests made to the /dvds/{id}/comments endpoint
     * @return HTTP Response to send to the user
     */
    private Response handlePostDVDComment(/* TODO: define and insert parameters */) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }


    /**
     * Creates a {@link DVD} record in the database from a {@code 'application/json'} content type
     * @param body JSON object containing the data needed to create a {@link DVD} record
     * @return HTTP Response to send to the user
     */
    @POST
    @JWTTokenNeeded
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postDVD(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            DVDResourceRequest body
    ) {
        String userId = this.getUserIdFromAuthorizationHeader(authorizationHeader);

        // This should only be triggered when the passed in JWT is referencing a user that has been deleted
        if (userId.isEmpty()) {
            DVDResourceResponse res = new DVDResourceResponse();
            res.setError(DVDS_ERROR_NO_CLIENT_ACCOUNT);
            return Response.status(Response.Status.UNAUTHORIZED).entity(res).build();
        }

        return this.handlePostDVDs(
                userId,
                body.getAgeLimit(),
                body.getDuration(),
                body.getTitle(),
                body.getType(),
                body.getDescription(),
                body.getEditor(),
                body.getAudio(),
                body.getReleaseDate()
        );
    }

    /**
     * Creates a {@link DVD} record in the database from a {@code 'application/x-www-form-urlencoded'} content type
     * @return HTTP Response to send to the user
     */
    @POST
    @JWTTokenNeeded
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postDVD(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @FormParam("ageLimit") String ageLimit,
            @FormParam("duration") String duration,
            @FormParam("title") String title,
            @FormParam("type") String type,
            @FormParam("description") String description,
            @FormParam("editor") String editor,
            @FormParam("audio") String audio,
            @FormParam("releaseDate") String releaseDate
    ) {
        String userId = this.getUserIdFromAuthorizationHeader(authorizationHeader);

        // This should only be triggered when the passed in JWT is referencing a user that has been deleted
        if (userId.isEmpty()) {
            DVDResourceResponse res = new DVDResourceResponse();
            res.setError(DVDS_ERROR_NO_CLIENT_ACCOUNT);
            return Response.status(Response.Status.UNAUTHORIZED).entity(res).build();
        }

        return this.handlePostDVDs(
                userId,
                ageLimit,
                duration,
                title,
                type,
                description,
                editor,
                audio,
                releaseDate
        );
    }

    /**
     * Creates a {@link DVD} {@code comment} record in the database from a {@code 'application/json'} content type
     * @param body JSON object containing the data needed to create a {@link DVD} {@code comment} record
     * @return HTTP Response to send to the user
     */
    @POST
    @Path("{id}/comments")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postDVDComment(
            /* TODO: create a simple class to hold the needed request parameters */
            Object body
    ) {
        return this.handlePostDVDComment();
    }

    /**
     * Creates a {@link DVD} {@code comment} record in the database from a {@code 'application/json'} content typ
     * @return HTTP Response to send to the user
     */
    @POST
    @Path("{id}/comments")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postDVDComment(
            /* TODO: define all the needed parameters */
    ) {
        return this.handlePostDVDComment();
    }
    ///endregion


    ///region PUT requests
    /**
     * Handle the {@code PUT} requests made to the /dvds/{id} endpoint
     * @return HTTP Response to send to the user
     */
    private Response handlePutDVDs(
            String authorizationHeader,
            String dvdId,
            String ageLimit,
            String duration,
            String title,
            String type,
            String description,
            String editor,
            String audio,
            String releaseDate
    ) {
        DVDResourceResponse res = new DVDResourceResponse();
        final String userId = this.getUserIdFromAuthorizationHeader(authorizationHeader);
        final DVD DVD = dvdDAO.findBy(dvdId);

        if (DVD == null) {
            res.setError(DVDS_ERROR_NOT_FOUND);
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }

        if (!userId.equals(DVD.getUserId())) {
            res.setError(DVDS_ERROR_FORBIDDEN);
            return Response.status(Response.Status.FORBIDDEN).entity(res).build();
        }

        // Update properties and update database record
        DVD.setAgeLimit(ageLimit);
        DVD.setDuration(duration);
        DVD.setTitle(title);
        DVD.setType(type);
        DVD.setDescription(description);
        DVD.setEditor(editor);
        DVD.setAudio(audio);
        DVD.setReleaseDate(releaseDate);
        dvdDAO.update(DVD);

        res.addItem(DVD);
        return Response.ok().entity(res).build();
    }

    /**
     * Handle the {@code PUT} requests made to the /dvds/{id} endpoint
     * @param dvdId Database record id of the {@link DVD} to update
     * @param body JSON representation of the {@link DVD} to update
     * @return HTTP response to send to the user
     */
    @PUT
    @JWTTokenNeeded
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putDVD(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathParam("id") String dvdId,
            DVDResourceRequest body
    ) {
        return this.handlePutDVDs(
                authorizationHeader,
                dvdId,
                body.getAgeLimit(),
                body.getDuration(),
                body.getTitle(),
                body.getType(),
                body.getDescription(),
                body.getEditor(),
                body.getAudio(),
                body.getReleaseDate()
        );
    }

    @PUT
    @JWTTokenNeeded
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putDVD(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathParam("id") String dvdId,
            @FormParam("ageLimit") String ageLimit,
            @FormParam("duration") String duration,
            @FormParam("title") String title,
            @FormParam("type") String type,
            @FormParam("description") String description,
            @FormParam("editor") String editor,
            @FormParam("audio") String audio,
            @FormParam("releaseDate") String releaseDate
    ) {
        return this.handlePutDVDs(
                authorizationHeader,
                dvdId,
                ageLimit,
                duration,
                title,
                type,
                description,
                editor,
                audio,
                releaseDate
        );
    }
    ///endregion


    ///region DELETE requests
    @DELETE
    @Path("{id}")
    @JWTTokenNeeded
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDVD(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathParam("id") String dvdId
    ) {
        DVDResourceResponse res = new DVDResourceResponse();
        String userId = this.getUserIdFromAuthorizationHeader(authorizationHeader);
        DVD DVD = dvdDAO.findBy(dvdId);

        if (DVD == null) {
            res.setError(DVDS_ERROR_NOT_FOUND);
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }

        // Check if user has the right to delete the resource
        if (!userId.equals(DVD.getUserId())) {
            res.setError(DVDS_ERROR_FORBIDDEN);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // Actually delete the resource
        dvdDAO.delete(DVD);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    ///endregion
}
