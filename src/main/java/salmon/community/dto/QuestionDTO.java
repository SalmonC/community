package salmon.community.dto;

import lombok.Data;
import salmon.community.model.User;

/**
 * @author SalmonC
 * @date 2021-08-30 21:29
 */
@Data
public class QuestionDTO {
    private int id;
    private String title;
    private String description;
    private String tag;
    private long gmtCreate;
    private long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}
