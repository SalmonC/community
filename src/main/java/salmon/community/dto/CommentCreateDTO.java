package salmon.community.dto;

import lombok.Data;

/**
 * @author SalmonC
 * @date 2021-09-04 01:58
 */
@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
