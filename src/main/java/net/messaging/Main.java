package net.messaging;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static Writer network;
    private static Writer console;
    private static Logger logger = Logger.getLogger(Main.class.getName());

    private static Map<String, String> parameterMap = new HashMap<>();

    public static void main(String... args) {
        Main obj = new Main();
        try {
            obj.buildParameterMap(args);
            obj.processInputCommand();
        }
        catch (SMTPClientException e) {
            logger.log(Level.SEVERE, e.getMessage()); //e.printStackTrace();
        }
    }

    private void processInputCommand() throws SMTPClientException {
        if (network == null) {
            network = new StringWriter();
        }

        try {
            network.append(SMTPClientConstants.CONNECT_STRING);
            network.append(" ");
            network.append(SMTPClientConstants.SMTP_PROTOCOL);
            network.append((char)10);

            network.append(SMTPClientConstants.TO_STRING);
            network.append(" ");
            network.append(parameterMap.get(SMTPClientConstants.RECEIVER_KEY));
            network.append((char)10);
            network.append((char)10);

            network.append(parameterMap.get(SMTPClientConstants.MESSAGE_KEY));
            network.append((char)10);
            network.append((char)10);

            network.append(SMTPClientConstants.DISCONNECT_STRING);
            network.append((char)10);
            network.flush();
        }
        catch (IOException e) {
            throw new SMTPClientException("Error while processing input command...");
        }
    }

    private void buildParameterMap(String[] args) {
        if (args == null || args.length < 2) {
            throw new IllegalArgumentException("Invalid input arguments...");
        }

        parameterMap.put(SMTPClientConstants.RECEIVER_KEY, args[0]);
        parameterMap.put(SMTPClientConstants.MESSAGE_KEY, args[1]);
    }

    public static void setNetwork(Writer network) {
        Main.network = network;
    }

    public static void setConsole(Writer console) {
        Main.console = console;
    }
}