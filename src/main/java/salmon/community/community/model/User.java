package salmon.community.community.model;

import lombok.Data;

/**
 * @author SalmonC
 * @date 2021-08-29 16:40
 */
@Data
public class User {
    private int id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;
}
