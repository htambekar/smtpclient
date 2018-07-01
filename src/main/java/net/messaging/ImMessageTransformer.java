package net.messaging;

import java.io.IOException;
import java.io.StringWriter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ImMessageTransformer extends AbstractMessageTransformer implements MessageTransformer {
    private static Logger logger = Logger.getLogger(ImMessageTransformer.class.getName());

    public ImMessageTransformer(String message, String[] receiver) {
        super(message, receiver);
    }

    @Override
    public void sendMessage() throws IOException {
        if (network == null) {
            network = new StringWriter();
        }

        network.append(TransformerConstants.CONNECT_STRING);
        network.append(" ");
        network.append(TransformerConstants.CHAT_PROTOCOL);
        network.append((char)10);

        for (String receiver : this.receivers) {
            network.append("<");
            network.append(receiver);
            network.append(">");
            network.append("(");
            network.append(this.message);
            network.append(")");
            network.append((char)10);
        }

        network.append(TransformerConstants.DISCONNECT_STRING);
        network.append((char)10);
        network.flush();
    }
}
