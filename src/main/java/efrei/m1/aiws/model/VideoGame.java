package efrei.m1.aiws.beans;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class VideoGame
{
    @Getter @Setter
    private int ID_VIDEO_GAME = 0;

    @Getter @Setter
    private int ID_USERS = 0;

    @Getter @Setter
    private String name = "";

    @Getter @Setter
    private String type = "";

    @Getter @Setter
    private String resume = "";

    @Getter @Setter
    private String video_game_editor = "";

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

    public VideoGame(int ID_VIDEO_GAME, int ID_USERS, String name, String type, String resume, String video_game_editor, String releaseDate, int rating)
    {
        this.ID_VIDEO_GAME = ID_VIDEO_GAME;
        this.ID_USERS = ID_USERS;
        this.name = name;
        this.type = type;
        this.resume = resume;
        this.video_game_editor = video_game_editor;
        this.releaseDate = releaseDate;
        this.rating = rating;
    }
}
