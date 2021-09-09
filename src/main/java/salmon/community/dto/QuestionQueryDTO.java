package salmon.community.dto;

import lombok.Data;

/**
 * @author SalmonC
 * @date 2021-09-08 23:39
 */
@Data
public class QuestionQueryDTO {
    private String search;
    private Integer offset;
    private Integer size;
}
