package efrei.m1.aiws.model;

import com.sun.xml.internal.ws.api.ha.StickyFeature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @NoArgsConstructor @AllArgsConstructor
public class Comment {

    private String id_Element;

    private String id_User;

    private String comment;

    //@TODO
    // Object VideoGame, Book et DVD ?


}
