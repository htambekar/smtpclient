package net.messaging;

import java.io.IOException;

public interface MessageTransformer {

    /**
     * Usually we shouldn't modify API, but since this api is only used internally,
     * modifying it will not make any harm.
     */
    public void sendMessage() throws IOException;

    public void displayErrorMessage(ClientException e);

    public void displayErrorMessage(String errorMessage);

    public void processInputCommand();
}
