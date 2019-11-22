package efrei.m1.aiws.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class User {
    private String mail;

    private String password;

    private String city;
}
