package pl.com.chrzanowski.sma.common.exception.error;

public enum EmailSendErrorCode implements IErrorCode {
    EMAIL_SEND_FAIL("emailSendFail");

    private final String code;

    EmailSendErrorCode(String code) {
        this.code = code;
    }


    @Override
    public String getCode() {
        return code;
    }
}
