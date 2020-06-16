/**
 * @Author: Willendless
 * @Date: 2020-06-14
 * @Description: LineMatch Exception
 * @LastEditTime: 2020-06-15
 * @FilePath: \code\LineMatchException.java
 */
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