package salmon.community.enums;

/**
 * @author SalmonC
 * @date 2021-09-07 15:08
 */
public enum NotificationStatusEnum {
    UNREAD(0),
    READ(1)
    ;

    private int status;

    public int getStatus() {
        return status;
    }

    NotificationStatusEnum(int status) {
        this.status = status;
    }
}
