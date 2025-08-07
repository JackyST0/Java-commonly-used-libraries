package com.example.testspringboot.exception;

import java.io.IOException;

/**
 * @author: 谭健新
 * @github: JackyST0
 * @date: 2025/1/14 13:33
 */
public class HttpConnectException extends IOException {
    private static final long serialVersionUID = 1L;

    public HttpConnectException(String message) {
        super(message);
    }

    public HttpConnectException(Throwable cause) {
        super(cause);
    }

    public static class UnauthorizedException extends HttpConnectException {
        private static final long serialVersionUID = 1L;

        public UnauthorizedException(String message) {
            super(message);
        }
    }

    public static class ForbiddenException extends HttpConnectException {
        private static final long serialVersionUID = 1L;

        public ForbiddenException(String message) {
            super(message);
        }
    }

    public static class NotAllowedException extends HttpConnectException {
        private static final long serialVersionUID = 1L;

        public NotAllowedException(String message) {
            super(message);
        }
    }

    public static class UnknownRequestException extends HttpConnectException {
        private static final long serialVersionUID = 1L;

        public UnknownRequestException(String message) {
            super(message);
        }
    }
}
