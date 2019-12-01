package efrei.m1.aiws.rest;


import efrei.m1.aiws.dao.DVDDAOImpl;
import efrei.m1.aiws.dao.UserDAOImpl;
import efrei.m1.aiws.model.DVD;
import efrei.m1.aiws.model.User;
import efrei.m1.aiws.service.JWTService;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Class representing the requests for JSON Objects
 */
@Data @NoArgsConstructor
 class DVDRessourceRequest{

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
 *
 * Class for the responses
 */

@Data @NoArgsConstructor
class DVDRessourceResponse{
    private String error = "";

    private List<DVD> items = new ArrayList<>();

    void addItem(DVD item) {
        this.items.add(item);
    }


        }



        @Path("/dvds")
        public class DVDRessource {

    @Setter
    private static DVDDAOImpl dvdDao;

    @Setter
    private static UserDAOImpl userDAO;

    //Checking if
    private String getUserIdFromAuthorizationHeader(String authorizationHeader) {
        final String jwtToken = JWTService.extractTokenFromHeader(authorizationHeader);
        User clientUserRecord = JWTService.getUserFromToken(jwtToken);

        // The following check should never be true
        if (clientUserRecord == null || clientUserRecord.getDbId() == null) {
                    return "";
                }

        return clientUserRecord.getDbId();
            }


}
