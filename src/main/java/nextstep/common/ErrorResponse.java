package nextstep.common;

public class ErrorResponse {
    private String msg;

    public ErrorResponse() {
    }

    public ErrorResponse(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
