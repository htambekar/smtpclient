package net.messaging;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.StringWriter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class AbstractMessageTransformerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testValidateInputParameters_validParameters() throws Exception {
        String[] receivers = {"joe@example.com"};
        SmtpMessageTransformer smtpProtocol = new SmtpMessageTransformer("Hi there!", receivers);

        smtpProtocol.validateInputParameters(); // shouldn't throw any exception.
        assertThat(smtpProtocol.getConsoleString(), nullValue());
    }

    @Test
    public void testValidateInputParameters_invalidReceiver() throws Exception {
        String[] receivers = { "joeexample.com" };
        StringWriter console = new StringWriter();
        SmtpMessageTransformer.setConsole(console);
        SmtpMessageTransformer smtpProtocol = new SmtpMessageTransformer("Hi there!", receivers);

        expectedException.expect(ClientException.class);
        expectedException.expectMessage("Invalid email address: joeexample.com");

        smtpProtocol.validateInputParameters(); // shouldn't throw any exception.
    }

    @Test
    public void testValidateInputParameters_invalidReceiver_multipleReceivers() throws Exception {
        String[] receivers = { "joeexample.com", "sallyexample.com" };
        StringWriter console = new StringWriter();
        SmtpMessageTransformer.setConsole(console);
        SmtpMessageTransformer smtpProtocol = new SmtpMessageTransformer("Hi there!", receivers);

        expectedException.expect(ClientException.class);
        expectedException.expectMessage("Invalid email addresses: joeexample.com sallyexample.com");

        smtpProtocol.validateInputParameters(); // shouldn't throw any exception.
    }

    @Test
    public void testValidateInputParameters_emptyMessageBody() throws Exception {
        String[] receivers = { "joe@example.com", "sally@example.com" };
        StringWriter console = new StringWriter();
        SmtpMessageTransformer.setConsole(console);
        SmtpMessageTransformer smtpProtocol = new SmtpMessageTransformer("", receivers);

        expectedException.expect(ClientException.class);
        expectedException.expectMessage("Cannot send an email with no body.");

        smtpProtocol.validateInputParameters(); // shouldn't throw any exception.
    }

    @Test
    public void testDisplayErrorMessage() throws Exception {
        String[] receivers = { "joe@example.com" };
        StringWriter console = new StringWriter();
        SmtpMessageTransformer.setConsole(console);
        SmtpMessageTransformer smtpProtocol = new SmtpMessageTransformer("Hi there!", receivers);

        ClientException clientException = new ClientException("error_message");
        smtpProtocol.displayErrorMessage(clientException);

        String consoleString = smtpProtocol.getConsoleString();
        assertThat(consoleString, equalTo("error_message\n"));
    }

    @Test
    public void testProcessInputCommand() throws Exception {
        String[] receivers = { "joe@example.com" };
        StringWriter console = new StringWriter();
        SmtpMessageTransformer.setConsole(console);
        SmtpMessageTransformer smtpProtocol = new SmtpMessageTransformer("Hi there!", receivers);

        smtpProtocol.processInputCommand();
        String consoleString = smtpProtocol.getConsoleString();
        assertThat(consoleString, equalTo(""));
    }

    @Test
    public void testProcessInputCommand_invalidReceiver() throws Exception {
        String[] receivers = { "joeexample.com" };
        StringWriter console = new StringWriter();
        SmtpMessageTransformer.setConsole(console);
        SmtpMessageTransformer smtpProtocol = new SmtpMessageTransformer("Hi there!", receivers);

        smtpProtocol.processInputCommand();
        String consoleString = smtpProtocol.getConsoleString();
        assertThat(consoleString, equalTo("Invalid email address: joeexample.com\n"));
    }
}
