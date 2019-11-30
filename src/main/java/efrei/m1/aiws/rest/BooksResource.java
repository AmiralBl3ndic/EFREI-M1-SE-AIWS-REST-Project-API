package efrei.m1.aiws.rest;


import efrei.m1.aiws.dao.BookDAOImpl;
import efrei.m1.aiws.model.Book;
import efrei.m1.aiws.model.User;
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
class BookResourceResquest {
    private String userID;
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
    private List<Book> bookItems = new ArrayList<>();

    void addBookItem(Book bookItem){
        this.bookItems.add(bookItem);
    }
}

@Path("/books")
public class BooksResource {

    @Setter
    private static BookDAOImpl bookDAO;

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
            @QueryParam("keywords") String keywordsParam
    ) {
        BookResourceResponse res = new BookResourceResponse();

        ArrayList<Book> books = (ArrayList<Book>) bookDAO.findAll();

        // Handle "keywords" url parameter
        if (keywordsParam != null && !keywordsParam.isEmpty()) {
            books.removeIf(book -> FuzzySearch.weightedRatio(book.getTitle(), keywordsParam) < FUZZY_SEARCH_MATCH_THRESHOLD);
        }

            // Handle "start" url parameter
        if (startParam != null && !startParam.isEmpty()) {
            // Check parameter value (must be of int type and greater than 0)
            int start;
            try {
                start = Integer.parseInt(startParam);
            } catch (NumberFormatException e) {
                res.setError(BOOKS_ILLEGAL_FILTER_TYPE_INT);
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity(res).build();
            }

            if (start < 0) {
                res.setError(BOOKS_ILLEGAL_FILTER_VALUE);
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity(res).build();
            }

            books.removeIf(book -> Integer.parseInt(book.getBookId()) < start);
        }

        // Handle "limit" url parameter
        if (limitParam != null) {
            // Check parameter value (must be of int type and greater than 0)
            int limit;
            try {
                limit = Integer.parseInt(limitParam);
            } catch (NumberFormatException e) {
                res.setError(BOOKS_ILLEGAL_FILTER_TYPE_INT);
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity(res).build();
            }

            if (limit < 0) {
                res.setError(BOOKS_ILLEGAL_FILTER_VALUE);
                return Response.status(Response.Status.NOT_ACCEPTABLE).entity(res).build();
            }
            if (limit < books.size()) {
                books = new ArrayList<>(books.subList(0, limit));
            }
        }
        res.setBookItems(books);
        return Response.ok().entity(res).build();
    }

    /**
     * Get the details of the book with database id {@code id}
     * @param bookID Database id of the book to get the details of
     * @return Details of a specific book if it has a record in the database, {@code 404 NOT_FOUND} otherwise
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookDetails(@PathParam("id") String bookID) {
        BookResourceResponse res = new BookResourceResponse();
        Book bookItem = bookDAO.findBy(bookID);

        if(bookID != null) {
            res.addBookItem(bookItem);
            return Response.ok().entity(res).build();
        } else {
            res.setError(BOOKS_ERROR_NOT_FOUND);
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
    }

    /**
     * Get the comments of the book with database id {@code id}
     * @param bookID Database id of the book to get the comments of
     * @return List tof comments of a specific book record if it has a record in the database, {@code 404 NOT_FOUND} otherwise
     */
    @GET
    @Path("{id}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookComments(@PathParam("id") String bookID) {
        BookResourceResponse res = new BookResourceResponse();
        Book bookItem = bookDAO.findBy(bookID);

        if(bookItem != null) {
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
     * @param userID Database id of the user that creates the record
     * @return HTTP Response to send to the user
     */
    private Response handlePostBooks(
            String userID,
            String author,
            String title,
            String type,
            String description,
            String releaseDate,
            String editor
    ) {
        BookResourceResponse res = new BookResourceResponse();
        Book newBook = new Book();

        newBook.setUserId(userID);
        newBook.setAuthor(author);
        newBook.setTitle(title);
        newBook.setType(type);
        newBook.setDescription(description);
        newBook.setReleaseDate(releaseDate);
        newBook.setEditor(editor);

        bookDAO.create(newBook);

        if(newBook.getBookId() == null) { // Check if request failed
            res.setError(BOOKS_ERROR_NOT_CREATED);
            return Response.status(Response.Status.NOT_MODIFIED).entity(res).build();
        } else {
            res.setError("");
            res.addBookItem(newBook);
            return Response.status(Response.Status.CREATED).entity(res).build();
        }
    }

    /**
     * Handle the {@code POST} requests made to the /books/{id}/comments endpoint
     * @return HTTP Response to send to the user
     */
    private Response handlePostBookComment(/* TODO : Implementer commentaires */) {
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
            BookResourceResquest body
    ) {
        String userID = this.getUserIdFromAuthorizationHeader(authorizationHeader);

        // This should only be triggered when the passed in JWT is referencing a user that has been deleted
        if(userID.isEmpty()) {
            BookResourceResponse res = new BookResourceResponse();
            res.setError(BOOKS_ERROR_NO_CLIENT_ACCOUNT);
            return Response.status(Response.Status.UNAUTHORIZED).entity(res).build();
        }

        return this.handlePostBooks(
                userID,
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
        String userID = this.getUserIdFromAuthorizationHeader(authorizationHeader);

        // This should only be triggered when the passed in JWT is referencing a user that has been deleted
        if(userID.isEmpty()) {
            BookResourceResponse res = new BookResourceResponse();
            res.setError(BOOKS_ERROR_NO_CLIENT_ACCOUNT);
            return Response.status(Response.Status.UNAUTHORIZED).entity(res).build();
        }

        return this.handlePostBooks(
                userID,
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
    private Response handlePutBooks(
            String authorizationHeader,
            String bookID,
            String author,
            String title,
            String type,
            String description,
            String releaseDate,
            String editor
    ) {
        BookResourceResponse res = new BookResourceResponse();
        final String userID = this.getUserIdFromAuthorizationHeader(authorizationHeader);
        final Book book = bookDAO.findBy(bookID);

        if(book == null) {
            res.setError(BOOKS_ERROR_NOT_FOUND);
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
        if(!userID.equals(book.getUserId())) {
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

        res.addBookItem(book);
        return Response.ok().entity(res).build();
    }

    /**
     * Handle the {@code PUT} requests made to the /books/{id} endpoint
     * @param bookID Database record id of the {@link Book} to update
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
            @PathParam("id") String bookID,
            BookResourceResquest body
    ) {
        return this.handlePutBooks(
                authorizationHeader,
                bookID,
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
    public Response putBook(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathParam("id") String bookID,
            @FormParam("author") String author,
            @FormParam("title") String title,
            @FormParam("type") String type,
            @FormParam("description") String description,
            @FormParam("releaseDate") String releaseDate,
            @FormParam("editor") String editor
    ) {
        return handlePutBooks(
                authorizationHeader,
                bookID,
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
    @JWTTokenNeeded
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBook(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathParam("id") String bookID
    ) {
        BookResourceResponse res = new BookResourceResponse();
        String userID = this.getUserIdFromAuthorizationHeader(authorizationHeader);
        Book book = bookDAO.findBy(bookID);

        if(bookID == null) {
            res.setError(BOOKS_ERROR_NOT_FOUND);
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }

        // Check if user has the right to delete the resource
        if(!userID.equals(book.getUserId())) {
            res.setError(BOOKS_ERROR_FORBIDDEN);
            return Response.status(Response.Status.FORBIDDEN).entity(res).build();
        }

        // Actually delete the resource
        bookDAO.delete(book);
        res.addBookItem(book);
        return Response.status(Response.Status.NO_CONTENT).entity(res).build();
    }
    ///endregion
}
