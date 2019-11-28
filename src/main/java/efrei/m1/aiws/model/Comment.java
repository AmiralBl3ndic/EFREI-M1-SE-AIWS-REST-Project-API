package efrei.m1.aiws.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class Comment<T> {

    private String content;

    private String creatorId;

    private String resourceId;

    private T resourceType;
}
