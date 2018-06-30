package net.messaging;

public class SMTPClientException extends Exception {
    public SMTPClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public SMTPClientException(String message) {
        super(message);
    }

    public SMTPClientException(Throwable cause) {
        super(cause);
    }
}
