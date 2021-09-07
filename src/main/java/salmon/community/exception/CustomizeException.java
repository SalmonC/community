package salmon.community.exception;

/**
 * @author SalmonC
 * @date 2021-09-03 18:12
 */
public class CustomizeException extends RuntimeException {
    private String message;
    private Integer code;

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    /**
     * @param errorCode 从自定义枚举类CustomizeException中选择一个合适的参数
     */
    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
    }
}
