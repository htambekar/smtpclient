package net.messaging;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractMessageTransformer implements MessageTransformer {
    protected static Writer network;
    protected static Writer console;

    protected String message;
    protected String[] receivers;

    private static Logger logger = Logger.getLogger(AbstractMessageTransformer.class.getName());

    public AbstractMessageTransformer(String message, String[] receivers) {
        this.message = message;
        this.receivers = receivers;
    }

    public abstract void sendMessage() throws IOException;

    public void validateInputParameters() throws ClientException {
        String exceptionString = "";
        int count = 0;
        for (String receiver : this.receivers) {
            if (count == 1 && !receiver.contains(TransformerConstants.EMAIL_VALIDATOR)) {
                exceptionString = exceptionString.replace(TransformerConstants.INVALID_EMAIL_ADDRESS, TransformerConstants.INVALID_EMAIL_ADDRESSES);
            }

            if (count > 0 && !receiver.contains(TransformerConstants.EMAIL_VALIDATOR)) {
                exceptionString += " " + receiver;
                count++;
            }
            else if (!receiver.contains(TransformerConstants.EMAIL_VALIDATOR)) {
                exceptionString += TransformerConstants.INVALID_EMAIL_ADDRESS + " " + receiver;
                count++;
            }
        }

        if (!exceptionString.isEmpty()) {
            throw new ClientException(exceptionString);
        }

        if (this.message == null || this.message.isEmpty()) {
            throw new ClientException(TransformerConstants.INVALID_MESSAGE_BODY);
        }
    }

    public void displayErrorMessage(ClientException e) {
        displayErrorMessage(e.getMessage());
    }

    public void displayErrorMessage(String errorMessage) {
        if (console == null) {
            console = new StringWriter();
        }

        if (errorMessage == null) {
            return;
        }

        try {
            console.append(errorMessage);
            console.append((char)10);
            console.flush();
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Error while writing to console..." + e.getMessage());
        }
    }

    public void processInputCommand() {
        try {
            validateInputParameters();
            sendMessage();
        }
        catch (IOException e) {
            displayErrorMessage(TransformerConstants.CONNECTION_ERROR);
        }
        catch (ClientException exception) {
            displayErrorMessage(exception);
        }
    }

    public static void setNetwork(Writer network) {
        AbstractMessageTransformer.network = network;
    }

    public static void setConsole(Writer console) {
        AbstractMessageTransformer.console = console;
    }

    protected String getNetworkString() {
        if (network == null) {
            return null;
        }

        return network.toString();
    }

    protected String getConsoleString() {
        if (console == null) {
            return null;
        }

        return console.toString();
    }
}
