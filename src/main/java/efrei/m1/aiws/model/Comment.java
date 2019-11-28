package efrei.m1.aiws.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @NoArgsConstructor @AllArgsConstructor
public class Comment {

    private String Id_Element;

    private String Id_User;

    private String Comment;

    private Object ElementType;

}
