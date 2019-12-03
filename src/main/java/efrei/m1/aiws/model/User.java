package efrei.m1.aiws.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @AllArgsConstructor @NoArgsConstructor
public class User {
    @ToString.Exclude
    private String dbId;

    private String email;

    private String password;

    private String city;
}
