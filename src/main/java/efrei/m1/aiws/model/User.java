package efrei.m1.aiws.model;

import lombok.*;

@Data
@ToString @NoArgsConstructor
public class User {

    @ToString.Exclude
    private int dbId;

    private String email;

    private String password; 

}
