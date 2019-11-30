package efrei.m1.aiws.rest;

import efrei.m1.aiws.dao.UserDAOImpl;
import efrei.m1.aiws.dao.BookDAOImpl;
import efrei.m1.aiws.model.User;
import efrei.m1.aiws.model.Book;
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

import efrei.m1.aiws.utils.Constants;

/**
 * Simple class to represent the incoming HTTP Request JSON Object
 */
@Data @NoArgsConstructor
class BookResourceRequest {
    private String userId;
    private String author;
    private String title;
    private String type;
    private String description;
    private String releaseDate;
    private String editor;
}

/**
 * Simple class to represent the outgoing HTTP Response JSON Object
 */
@Data @NoArgsConstructor
class BookResourceResponse {
    private String error = "";

    private List<Book> items = new ArrayList<>();

    void addItem(Book item) {
        this.items.add(item);
    }
}


@Path("/books")
public class BooksResource {

    @Setter
    private static BookDAOImpl bookDAO;

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
     * @param books List of {@link Book}s to perform the filtering on
     * @param limitParam Maximum number of items to return
     * @param startParam Minimum (inclusive) id of the records to return
     * @param keywordsParam Keywords to look for (records below {@code FUZZY_SEARCH_MATCH_THRESHOLD} similarity will be excluded)
     * @param creatorParam Id of the user who created the record
     * @param cityParam City to look for records in
     * @return List of {@link Book}s that meet all the filters requirements
     */
    private ArrayList<Book> applyGetUrlParameters(
            ArrayList<Book> books,
            String limitParam,
            String startParam,
            String keywordsParam,
            String creatorParam,
            String cityParam
    ) {
        // Handle "creator" url parameter
        if (creatorParam != null && !creatorParam.isEmpty()) {
            books.removeIf(book -> !book.getUserId().equals(creatorParam));
        }

        // Handle "keywords" url parameter
        if (keywordsParam != null && !keywordsParam.isEmpty()) {
            books.removeIf(book -> FuzzySearch.weightedRatio(book.getTitle(), keywordsParam) < FUZZY_SEARCH_MATCH_THRESHOLD);
        }

        // Handle "city" url parameter
        if (cityParam != null && !cityParam.isEmpty()) {
            books.removeIf(book -> {
                // Gather creator of video-game record
                User creator = userDAO.findBy(book.getUserId());
                if (creator == null) {
                    return true;
                }

                return !creator.getCity().equalsIgnoreCase(cityParam);
            });
        }

        // Handle "start" url parameter
        if (startParam != null && !startParam.isEmpty()) {
            int minId = Integer.parseInt(startParam);
            books.removeIf(book -> Integer.parseInt(book.getBookId()) < minId);
        }

        // Handle "limit" url parameter
        if (limitParam != null && !limitParam.isEmpty()) {
            int maxRecords = Integer.parseInt(limitParam);
            books = new ArrayList<>(books.subList(0, maxRecords));
        }

        return books;
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
            return BOOKS_ILLEGAL_FILTER_TYPE_INT;
        }

        if (paramValue < 0) {
            return BOOKS_ILLEGAL_FILTER_VALUE;
        }

        return "";
    }


    ///region GET requests
    /**
     * Get the list of all books records in the database
     * @return List of all books records in the database
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooks(
            @QueryParam("limit") String limitParam,
            @QueryParam("start") String startParam,
            @QueryParam("keywords") String keywordsParam,
            @QueryParam("creator") String creatorParam,
            @QueryParam("city") String cityParam
    ) {
        BookResourceResponse res = new BookResourceResponse();

        ArrayList<Book> books = (ArrayList<Book>) bookDAO.findAll();

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
        books = this.applyGetUrlParameters(books, limitParam, startParam, keywordsParam, creatorParam, cityParam);

        res.setItems(books);
        return Response.ok().entity(res).build();
    }

    /**
     * Get the details of the book with database id {@code id}
     * @param bookId Database id of the book to get the details of
     * @return Details of a specific book if it has a record in the database, {@code 404 NOT_FOUND} otherwise
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookDetails(@PathParam("id") String bookId) {
        BookResourceResponse res = new BookResourceResponse();
        Book item = bookDAO.findBy(bookId);

        if (item != null) {
            res.addItem(item);
            return Response.ok().entity(res).build();
        } else {
            res.setError(BOOKS_ERROR_NOT_FOUND);
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
    }

    /**
     * Get the comments of the book with database id {@code id}
     * @param bookId Database id of the book to get the comments of
     * @return List tof comments of a specific book record if it has a record in the database, {@code 404 NOT_FOUND} otherwise
     */
    @GET
    @Path("{id}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookComments(@PathParam("id") String bookId) {
        BookResourceResponse res = new BookResourceResponse();
        Book item = bookDAO.findBy(bookId);

        if (item != null) {
            res.setError(BOOKS_ERROR_NOT_FOUND);
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        } else {
            return Response.status(Response.Status.NOT_IMPLEMENTED).entity(res).build();
        }
    }
    ///endregion


    ///region POST requests
    /**
     * Handle the {@code POST} requests made to the /books endpoint
     * @param userId Database id of the user that creates the record
     * @return HTTP Response to send to the user
     */
    private Response handlePostBooks(
            String userId,
            String author,
            String title,
            String type,
            String description,
            String releaseDate,
            String editor
    ) {
        BookResourceResponse res = new BookResourceResponse();
        Book newRecord = new Book();

        newRecord.setUserId(userId);
        newRecord.setAuthor(author);
        newRecord.setTitle(title);
        newRecord.setType(type);
        newRecord.setDescription(description);
        newRecord.setReleaseDate(releaseDate);
        newRecord.setEditor(editor);

        bookDAO.create(newRecord);

        if (newRecord.getBookId() == null) {  // Check if request failed
            res.setError(BOOKS_ERROR_NOT_CREATED);
            return Response.status(Response.Status.NOT_MODIFIED).entity(res).build();
        } else {
            res.setError("");
            res.addItem(newRecord);
            return Response.status(Response.Status.CREATED).entity(res).build();
        }
    }

    /**
     * Handle the {@code POST} requests made to the /books/{id}/comments endpoint
     * @return HTTP Response to send to the user
     */
    private Response handlePostBookComment(/* TODO: define and insert parameters */) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }


    /**
     * Creates a {@link Book} record in the database from a {@code 'application/json'} content type
     * @param body JSON object containing the data needed to create a {@link Book} record
     * @return HTTP Response to send to the user
     */
    @POST
    @JWTTokenNeeded
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postBook(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            BookResourceRequest body
    ) {
        String userId = this.getUserIdFromAuthorizationHeader(authorizationHeader);

        // This should only be triggered when the passed in JWT is referencing a user that has been deleted
        if (userId.isEmpty()) {
            BookResourceResponse res = new BookResourceResponse();
            res.setError(BOOKS_ERROR_NO_CLIENT_ACCOUNT);
            return Response.status(Response.Status.UNAUTHORIZED).entity(res).build();
        }

        return this.handlePostBooks(
                userId,
                body.getAuthor(),
                body.getTitle(),
                body.getType(),
                body.getDescription(),
                body.getReleaseDate(),
                body.getEditor()
        );
    }

    /**
     * Creates a {@link Book} record in the database from a {@code 'application/x-www-form-urlencoded'} content type
     * @return HTTP Response to send to the user
     */
    @POST
    @JWTTokenNeeded
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postBook(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @FormParam("author") String author,
            @FormParam("title") String title,
            @FormParam("type") String type,
            @FormParam("description") String description,
            @FormParam("releaseDate") String releaseDate,
            @FormParam("editor") String editor
    ) {
        String userId = this.getUserIdFromAuthorizationHeader(authorizationHeader);

        // This should only be triggered when the passed in JWT is referencing a user that has been deleted
        if (userId.isEmpty()) {
            BookResourceResponse res = new BookResourceResponse();
            res.setError(BOOKS_ERROR_NO_CLIENT_ACCOUNT);
            return Response.status(Response.Status.UNAUTHORIZED).entity(res).build();
        }

        return this.handlePostBooks(
                userId,
                author,
                title,
                type,
                description,
                releaseDate,
                editor
        );
    }

    /**
     * Creates a {@link Book} {@code comment} record in the database from a {@code 'application/json'} content type
     * @param body JSON object containing the data needed to create a {@link Book} {@code comment} record
     * @return HTTP Response to send to the user
     */
    @POST
    @Path("{id}/comments")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postBookComment(
            /* TODO: create a simple class to hold the needed request parameters */
            Object body
    ) {
        return this.handlePostBookComment();
    }

    /**
     * Creates a {@link Book} {@code comment} record in the database from a {@code 'application/json'} content typ
     * @return HTTP Response to send to the user
     */
    @POST
    @Path("{id}/comments")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postBookComment(
            /* TODO: define all the needed parameters */
    ) {
        return this.handlePostBookComment();
    }
    ///endregion


    ///region PUT requests
    /**
     * Handle the {@code PUT} requests made to the /books/{id} endpoint
     * @return HTTP Response to send to the user
     */
    private Response handlePutBooks (
            String authorizationHeader,
            String bookId,
            String author,
            String title,
            String type,
            String description,
            String releaseDate,
            String editor
    ) {
        BookResourceResponse res = new BookResourceResponse();
        final String userId = this.getUserIdFromAuthorizationHeader(authorizationHeader);
        final Book book = bookDAO.findBy(bookId);

        if (book == null) {
            res.setError(BOOKS_ERROR_NOT_FOUND);
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }

        if (!userId.equals(book.getUserId())) {
            res.setError(BOOKS_ERROR_FORBIDDEN);
            return Response.status(Response.Status.FORBIDDEN).entity(res).build();
        }

        // Update properties and update database record
        book.setAuthor(author);
        book.setTitle(title);
        book.setType(type);
        book.setDescription(description);
        book.setReleaseDate(releaseDate);
        book.setEditor(editor);
        bookDAO.update(book);

        res.addItem(book);
        return Response.ok().entity(res).build();
    }

    /**
     * Handle the {@code PUT} requests made to the /books/{id} endpoint
     * @param bookId Database record id of the {@link Book} to update
     * @param body JSON representation of the {@link Book} to update
     * @return HTTP response to send to the user
     */
    @PUT
    @JWTTokenNeeded
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putBook(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathParam("id") String bookId,
            BookResourceRequest body
    ) {
        return this.handlePutBooks(
                authorizationHeader,
                bookId,
                body.getAuthor(),
                body.getTitle(),
                body.getType(),
                body.getDescription(),
                body.getReleaseDate(),
                body.getEditor()
        );
    }

    @PUT
    @JWTTokenNeeded
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putBook (
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathParam("id") String bookId,
            @FormParam("author") String author,
            @FormParam("title") String title,
            @FormParam("type") String type,
            @FormParam("description") String description,
            @FormParam("releaseDate") String releaseDate,
            @FormParam("Editor") String editor
    ) {
        return this.handlePutBooks(
                authorizationHeader,
                bookId,
                author,
                title,
                type,
                description,
                releaseDate,
                editor
        );
    }
    ///endregion


    ///region DELETE requests
    @DELETE
    @Path("{id}")
    @JWTTokenNeeded
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBook (
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathParam("id") String bookId
    ) {
        BookResourceResponse res = new BookResourceResponse();
        String userId = this.getUserIdFromAuthorizationHeader(authorizationHeader);
        Book book = bookDAO.findBy(bookId);

        if (book == null) {
            res.setError(BOOKS_ERROR_NOT_FOUND);
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }

        // Check if user has the right to delete the resource
        if (!userId.equals(book.getUserId())) {
            res.setError(BOOKS_ERROR_FORBIDDEN);
            return Response.status(Response.Status.FORBIDDEN).entity(res).build();
        }

        // Actually delete the resource
        bookDAO.delete(book);
        res.addItem(book);
        return Response.status(Response.Status.NO_CONTENT).entity(res).build();
    }
    ///endregion
}
