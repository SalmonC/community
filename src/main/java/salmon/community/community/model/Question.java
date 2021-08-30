package salmon.community.community.model;

import lombok.Data;

/**
 * @author SalmonC
 * @date 2021-08-29 23:03
 */
@Data
public class Question {
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
}
