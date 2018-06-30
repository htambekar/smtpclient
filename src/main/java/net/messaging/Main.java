package net.messaging;

import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static Writer network;
    private static Writer console;
    private static Logger logger = Logger.getLogger(Main.class.getName());

    private static Map<String, Object> parameterMap = new HashMap<>();

    public static void main(String... args) {
        Main obj = new Main();
        try {
            obj.buildParameterMap(args);
            obj.validateInputParameters();
            obj.processInputCommand();
        }
        catch (SMTPClientException exception) {
            obj.processErrorMessage(exception);
        }
    }

    protected void validateInputParameters() throws SMTPClientException {
        String[] receivers = (String[]) parameterMap.get(SMTPClientConstants.RECEIVER_KEY);
        String exceptionString = "";
        int count = 0;
        for (String receiver : receivers) {
            if (count == 1 && !receiver.contains(SMTPClientConstants.EMAIL_VALIDATOR)) {
                exceptionString = exceptionString.replace(SMTPClientConstants.INVALID_EMAIL_ADDRESS, SMTPClientConstants.INVALID_EMAIL_ADDRESSES);
            }

            if (count > 0 && !receiver.contains(SMTPClientConstants.EMAIL_VALIDATOR)) {
                exceptionString += " " + receiver;
                count++;
            }
            else if (!receiver.contains(SMTPClientConstants.EMAIL_VALIDATOR)) {
                exceptionString += SMTPClientConstants.INVALID_EMAIL_ADDRESS + " " + receiver;
                count++;
            }
        }

        if (!exceptionString.isEmpty()) {
            throw new SMTPClientException(exceptionString);
        }

        String messageBody = (String)parameterMap.get(SMTPClientConstants.MESSAGE_KEY);
        if (messageBody == null || messageBody.isEmpty()) {
            throw new SMTPClientException(SMTPClientConstants.INVALID_MESSAGE_BODY);
        }
    }

    protected void processErrorMessage(SMTPClientException exception) {
        if (console == null) {
            console = new StringWriter();
        }

        try {
            console.append(exception.getMessage());
            console.append((char)10);
            console.flush();
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Error while writing to console..." + e.getMessage());
        }
    }

    protected void processInputCommand() {
        if (network == null) {
            network = new StringWriter();
        }

        try {
            network.append(SMTPClientConstants.CONNECT_STRING);
            network.append(" ");
            network.append(SMTPClientConstants.SMTP_PROTOCOL);
            network.append((char)10);

            String[] receivers = (String[])parameterMap.get(SMTPClientConstants.RECEIVER_KEY);
            for (String receiver : receivers) {
                network.append(SMTPClientConstants.TO_STRING);
                network.append(" ");
                network.append(receiver);
                network.append((char)10);
            }
            network.append((char)10);

            network.append((String)parameterMap.get(SMTPClientConstants.MESSAGE_KEY));
            network.append((char)10);
            network.append((char)10);

            network.append(SMTPClientConstants.DISCONNECT_STRING);
            network.append((char)10);
            network.flush();
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Error while processing input command..." + e.getMessage());
        }
    }

    protected void buildParameterMap(String[] args) {
        if (args == null || args.length < 2) {
            throw new IllegalArgumentException("Invalid input arguments...");
        }

        String receiverString = args[0];
        String[] receivers = receiverString.split(SMTPClientConstants.RECEIVER_SEPARATOR);
        parameterMap.put(SMTPClientConstants.RECEIVER_KEY, receivers);
        parameterMap.put(SMTPClientConstants.MESSAGE_KEY, args[1]);
    }

    public static void setNetwork(Writer network) {
        Main.network = network;
    }

    public static void setConsole(Writer console) {
        Main.console = console;
    }

    // for unit tests.
    protected Map<String, Object> getParameterMap() {
        return parameterMap;
    }
}