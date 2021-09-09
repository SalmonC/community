package salmon.community.exception;

/**
 * @author SalmonC
 * @date 2021-09-03 20:02
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND(2001,"该问题不存在了, 换一个试试?"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题或评论进行回复!"),
    NO_LOGIN(2003,"未登录不能进行该操作, 请登录后重试"),
    SYSTEM_ERROR(2004,"服务器出错, 请直接联系作者三文鱼, 或者出去跑两圈再来试试吧!"),
    TYPE_PARAM_WRONG(2005,"评论类型错误, 重试一下吧!"),
    COMMENT_NOT_FOUND(2006,"你找的评论已经不在了, 刷新一下吧! "),
    CONTENT_IS_EMPTY(2007,"输入内容不能为空! "),
    READ_NOTIFICATION_FAIL(2008,"你这是读别人的消息, 重开一下试试? "),
    NOTIFICATION_NOT_FOUND(2009,"消息不翼而飞了, 刷新一下试试吧! "),
    FILE_UPLOAD_FAIL(2010,"图片上传失败"),
    NO_AUTHORITY(2011,"没有权限执行此操作!");
    private String message;
    private Integer code;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }


    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
