package salmon.community.dto;

import lombok.Data;
import salmon.community.model.User;

/**
 * @author SalmonC
 * @date 2021-09-07 15:43
 */
@Data
public class NotificationDTO {
    private Long id;
    private Long outerid;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private String notifierName;
    private String outerTitle;
    /** 评论类型的文字描述 参考enum*/
    private String typeName;
    private Integer type;
    private Boolean isUnread;
}
