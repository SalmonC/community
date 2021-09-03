package salmon.community.exception;

/**
 * @author SalmonC
 * @date 2021-09-03 18:12
 */
public class CustomizeException extends RuntimeException {
    private  String message;

    @Override
    public String getMessage() {
        return  message;
    }

    public CustomizeException(String message) {
        this.message = message;
    }

    public CustomizeException(ICustomizeErrorCode errorCode) {
        this.message = errorCode.getMessage();
    }
}
