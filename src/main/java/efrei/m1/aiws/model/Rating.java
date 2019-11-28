package efrei.m1.aiws.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.management.ObjectName;

@Data @NoArgsConstructor
public class Rating {

    private static final int DEFAULT_RATING = 0;
    private static final int MINIMUM_RATING = 0;
    private static final int MAXIMUM_RATING = 5;


    private  String idObject;
    private String idUser;
    private int rating;
    private Object elementRated;


    public void setRating(int rating) {
        if(rating >= MINIMUM_RATING && rating <= MAXIMUM_RATING) {
            this.rating = rating;
        }
    }

    public Rating(String iduser, String idobj, int rating, Object obj) {
        this.idUser=iduser;
        this.idObject=idobj;
        this.elementRated=obj;
        this.rating = rating >= MINIMUM_RATING && rating <= MAXIMUM_RATING ? rating : DEFAULT_RATING;
    }

}
