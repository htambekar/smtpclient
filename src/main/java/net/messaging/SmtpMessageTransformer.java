package net.messaging;

import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SmtpMessageTransformer extends AbstractMessageTransformer implements MessageTransformer {

    private static Logger logger = Logger.getLogger(SmtpMessageTransformer.class.getName());

    public SmtpMessageTransformer(String message, String[] receivers) {
        super(message, receivers);
    }

    @Override
    public void sendMessage() throws IOException {
        if (network == null) {
            network = new StringWriter();
        }

        network.append(TransformerConstants.CONNECT_STRING);
        network.append(" ");
        network.append(TransformerConstants.SMTP_PROTOCOL);
        network.append((char)10);

        for (String receiver : this.receivers) {
            network.append(TransformerConstants.TO_STRING);
            network.append(" ");
            network.append(receiver);
            network.append((char)10);
        }
        network.append((char)10);

        network.append(this.message);
        network.append((char)10);
        network.append((char)10);

        network.append(TransformerConstants.DISCONNECT_STRING);
        network.append((char)10);
        network.flush();
    }
}
