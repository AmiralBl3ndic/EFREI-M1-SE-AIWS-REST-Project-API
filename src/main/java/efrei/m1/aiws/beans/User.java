package efrei.m1.aiws.beans;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class User
{
    @Getter
    @Setter
    private String mail = "";

    @Getter
    @Setter
    private String password = "";

    @Getter
    @Setter
    private String city = "";

    public User(String mail, String password, String city)
    {
        this.mail = mail;
        this.password = password;
        this.city = city;
    }
}
