package salmon.community.dto;

import lombok.Data;

import java.util.List;

/**
 * @author SalmonC
 * @date 2021-09-07 01:19
 */
@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;

}
