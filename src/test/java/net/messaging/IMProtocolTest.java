package net.messaging;

import org.junit.Test;

import java.io.StringWriter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class IMProtocolTest {

    @Test
    public void testSendMessage() throws Exception {
        String[] receivers = {"joe@example.com"};
        ImMessageTransformer imProtocol = new ImMessageTransformer("Hi there!", receivers);
        ImMessageTransformer.setNetwork(new StringWriter());
        imProtocol.sendMessage();

        String message = imProtocol.getNetworkString();
        assertThat(message, equalTo("connect chat\n<joe@example.com>(Hi there!)\ndisconnect\n"));
    }

    @Test
    public void testSendMessage_multipleUsers() throws Exception {
        String[] receivers = {"joe@example.com", "sally@example.com"};
        ImMessageTransformer imProtocol = new ImMessageTransformer("Hi there!", receivers);
        ImMessageTransformer.setNetwork(new StringWriter());
        imProtocol.sendMessage();

        String message = imProtocol.getNetworkString();
        assertThat(message, equalTo("connect chat\n<joe@example.com>(Hi there!)\n<sally@example.com>(Hi there!)\ndisconnect\n"));
    }
}
