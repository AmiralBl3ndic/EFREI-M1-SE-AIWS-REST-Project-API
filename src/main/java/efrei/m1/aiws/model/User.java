package efrei.m1.aiws.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data @AllArgsConstructor
public class User {
    @ToString.Exclude
    private String dbId;

    private String mail;

    private String password;

    private String city;
}
