package net.messaging;

public interface MessageTransformer {

    public void sendMessage();

    public void displayErrorMessage(ClientException e);

    public void processInputCommand();
}
