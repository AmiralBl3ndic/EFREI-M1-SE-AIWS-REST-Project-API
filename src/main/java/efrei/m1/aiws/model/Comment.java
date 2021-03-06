package efrei.m1.aiws.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class Comment {

    private String dbId;

    private String content;

    private String creatorId;

    private String resourceId;
}
