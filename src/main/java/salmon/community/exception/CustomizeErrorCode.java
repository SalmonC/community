package salmon.community.exception;

/**
 * @author SalmonC
 * @date 2021-09-03 20:02
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND("该问题不存在了, 换一个试试?");
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    CustomizeErrorCode(String message) {
        this.message = message;
    }
}
