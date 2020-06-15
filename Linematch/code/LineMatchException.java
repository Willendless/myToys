package code;

public class LineMatchException extends RuntimeException {

    private String _msg;

    LineMatchException() {
        super();
    }

    LineMatchException(String msg) {
        _msg = msg;
    }

    public String getMsg() {
        return _msg;
    }
    
}