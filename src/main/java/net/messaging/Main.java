package net.messaging;

import java.io.*;

public class Main {
    private MessageTransformer protocol = null;

    public static void main(String... args) {
        Main obj = new Main();
        obj.buildProtocol(args);
    }

    public static void setNetwork(Writer network) {
        AbstractMessageTransformer.setNetwork(network);
    }

    public static void setConsole(Writer console) {
        AbstractMessageTransformer.setConsole(console);
    }

    public void buildProtocol(String[] args) {
        if (args == null || args.length < 2) {
            throw new IllegalArgumentException("Invalid input arguments...");
        }

        if (args.length == 3) {
            String protocolString = args[0];
            String receiverString = args[1];
            String[] receivers = receiverString.split(TransformerConstants.RECEIVER_SEPARATOR);
            String message = args[2];

            if (protocolString.equals(TransformerConstants.IM_PROTOCOL)) {
                this.protocol = new ImMessageTransformer(message, receivers);
            }
            else if (protocolString.equals(TransformerConstants.SMTP_PROTOCOL)) {
                this.protocol = new SmtpMessageTransformer(message, receivers);
            }
            else {
                throw new IllegalArgumentException("Unknown protocol...");
            }
        }
        else if (args.length == 2) {
            String receiverString = args[0];
            String[] receivers = receiverString.split(TransformerConstants.RECEIVER_SEPARATOR);
            String message = args[1];
            this.protocol = new SmtpMessageTransformer(message, receivers);
        }

        this.protocol.processInputCommand();
    }
}