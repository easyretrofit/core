package io.github.easyretrofit.core.exception;

/**
 * @author liuziyuan
 */
public class RetrofitBaseException extends RuntimeException {

    public RetrofitBaseException(String message) {
        super(message);
    }

    public RetrofitBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
