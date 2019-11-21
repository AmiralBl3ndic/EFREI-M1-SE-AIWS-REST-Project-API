package efrei.m1.aiws.beans;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class DVD
{
    @Getter @Setter
    private int ID_DVD = 0;

    @Getter @Setter
    private int ID_USER = 0;

    @Getter @Setter
    private int ageLimit = 0;

    @Getter @Setter
    private int duration = 0;

    @Getter @Setter
    private String title = "";

    @Getter @Setter
    private String type = "";

    @Getter @Setter
    private String description = "";

    @Getter @Setter
    private String editor = "";

    @Getter @Setter
    private String audio = "";

    @Getter @Setter
    private String releaseDate = "";

    //Manual Setter to check if the rate is between 0 and 5
    private int rating = 0;
    public int getRating() {return rating;}
    public void setRating(int rating)
    {
        if(rating>=0 && rating <= 6)
            this.rating = rating;
    }

    public DVD(int ID_DVD, int ID_USER, int ageLimit, int duration, String title, String type, String description, String editor, String audio, String releaseDate, int rating)
    {
        this.ID_DVD = ID_DVD;
        this.ID_USER = ID_USER;
        this.ageLimit = ageLimit;
        this.duration = duration;
        this.title = title;
        this.type = type;
        this.description = description;
        this.editor = editor;
        this.audio = audio;
        this.releaseDate = releaseDate;
        this.rating = rating;
    }





}
