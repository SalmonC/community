package salmon.community.dto;

import lombok.Data;
import salmon.community.model.User;

/**
 * @author SalmonC
 * @date 2021-09-05 19:37
 */
@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private Integer commentCount;
    private String content;
    private User user;
}
