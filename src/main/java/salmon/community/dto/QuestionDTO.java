package salmon.community.dto;

import lombok.Data;
import salmon.community.model.User;

/**
 * @author SalmonC
 * @date 2021-08-30 21:29
 */
@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}
