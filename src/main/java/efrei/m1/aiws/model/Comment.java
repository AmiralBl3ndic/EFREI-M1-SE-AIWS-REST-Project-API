package efrei.m1.aiws.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @NoArgsConstructor @AllArgsConstructor
public class Comment {

    private String id_Element;

    private String id_User;

    private String comment;

    private Object elementType;

}
