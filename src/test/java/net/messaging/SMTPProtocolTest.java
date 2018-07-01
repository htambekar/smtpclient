package net.messaging;

import org.junit.Test;

import java.io.StringWriter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class SMTPProtocolTest {

    @Test
    public void testSendMessage() throws Exception {
        String[] receivers = {"joe@example.com"};
        SmtpMessageTransformer smtpProtocol = new SmtpMessageTransformer("Hi there!", receivers);
        SmtpMessageTransformer.setNetwork(new StringWriter());
        smtpProtocol.sendMessage();

        String message = smtpProtocol.getNetworkString();
        assertThat(message, equalTo("connect smtp\nTo: joe@example.com\n\nHi there!\n\ndisconnect\n"));
    }

    @Test
    public void testSendMessage_multipleReceivers() throws Exception {
        String[] receivers = {"joe@example.com", "sally@example.com"};
        SmtpMessageTransformer smtpProtocol = new SmtpMessageTransformer("Hi there!", receivers);
        SmtpMessageTransformer.setNetwork(new StringWriter());
        smtpProtocol.sendMessage();

        String message = smtpProtocol.getNetworkString();
        assertThat(message, equalTo("connect smtp\nTo: joe@example.com\nTo: sally@example.com\n\nHi there!\n\ndisconnect\n"));
    }
}
