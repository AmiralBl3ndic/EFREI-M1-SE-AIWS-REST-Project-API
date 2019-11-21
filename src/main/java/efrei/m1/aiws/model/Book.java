package efrei.m1.aiws.beans;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Book
{
    @Getter @Setter
    int ID_BOOK = 0;

    @Getter @Setter
    int ID_USER = 0;

    @Getter @Setter
    String author = "";

    @Getter @Setter
    String title = "";

    @Getter @Setter
    String type = "";

    @Getter @Setter
    String description = "";

    @Getter @Setter
    String releaseDate = "";

    @Getter @Setter
    String editor = "";

    @Getter @Setter
    String plateform = "";

    @Getter @Setter
    int ageLimit = 0;

    //Manual Setter to check if the rate is between 0 and 5
    private int rating = 0;
    public int getRating() {return rating;}
    public void setRating(int rating)
    {
        if(rating>=0 && rating <= 6)
            this.rating = rating;
    }

    public Book(int ID_USER, int ID_BOOK, String author, String title, String type, String description, String releaseDate, String editor, String plateform, int ageLimit, int rating)
    {
        this.ID_USER = ID_USER;
        this.ID_BOOK = ID_BOOK;
        this.author = author;
        this.title = title;
        this.type = type;
        this.description = description;
        this.releaseDate = releaseDate;
        this.editor = editor;
        this.plateform = plateform;
        this.ageLimit = ageLimit;
        this.rating = rating;
    }

}
