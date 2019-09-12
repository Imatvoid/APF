package com.arch.ability.cmd;

/**
 * 业务能力扩展点的反馈基础类
 */
public abstract class BaseReply {
    private static final int OK = 1;
    private static final int FAIL = 2;

    /**
     * 状态码，默认是成功的
     */
    private int statusCode = OK;

    /**
     * 错误消息编号，对应I18N的message.properties里的一行
     */
    private String resultErrorCode;

    /**
     * 用于填充{@link #resultErrorCode}对应的message
     */
    private String[] resultErrorMsg;

    /**
     * 用于向调用方提供异常信息
     */
    private transient Throwable cause;

    public final boolean isOk() {
        return statusCode == OK;
    }
    
    public final void fail(String msgKey, String... params) {
        this.statusCode = FAIL;

        this.resultErrorCode = msgKey;
        this.resultErrorMsg = params;
    }

    public final void fail(Throwable cause, String msgKey, String... params) {
        this.statusCode = FAIL;

        this.resultErrorCode = msgKey;
        this.cause = cause;
        this.resultErrorMsg = params;
    }

    public final String getResultErrorCode() {
        return resultErrorCode;
    }

    public final String[] getResultErrorMsg() {
        return resultErrorMsg;
    }

    public final Throwable getCause() {
        return cause;
    }

}
