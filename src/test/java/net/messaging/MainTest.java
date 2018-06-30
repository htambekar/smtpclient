package net.messaging;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Map;
import java.io.StringWriter;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.containsString;

public class MainTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testBuildParameterMap() {
        Main obj = new Main();
        String[] args = new String[2];
        args[0] = "joe@example.com";
        args[1] = "Hi there!";

        obj.buildParameterMap(args);
        Map<String, Object> parameterMap = obj.getParameterMap();
        assertThat(parameterMap, notNullValue());
        assertThat(parameterMap.size(), equalTo(2));
    }

    @Test
    public void testBuildParameterMap_emptyMessage_expectIllegalArgumentExpception() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid input arguments...");

        Main obj = new Main();
        String[] args = new String[1];
        args[0] = "joe@example.com";

        obj.buildParameterMap(args);
    }

    @Test
    public void testBuildParameterMap_nullArguments_expectIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid input arguments...");

        Main obj = new Main();
        obj.buildParameterMap(null);
    }

    @Test
    public void testValidateInputParameters_validEmailAddress() throws Exception {
        Main obj = new Main();
        String[] args = new String[2];
        args[0] = "joe@example.com";
        args[1] = "Hi there!";

        obj.buildParameterMap(args);
        obj.validateInputParameters();
    }

    @Test
    public void testValidateInputParameters_InvalidEmailAddress_expectException() throws Exception {
        expectedException.expect(SMTPClientException.class);
        expectedException.expectMessage(SMTPClientConstants.INVALID_EMAIL_ADDRESS);

        Main obj = new Main();
        String[] args = new String[2];
        args[0] = "joeexample.com";
        args[1] = "Hi there!";

        obj.buildParameterMap(args);
        obj.validateInputParameters();
    }

    @Test
    public void testProcessErrorMessage() throws Exception {
        StringWriter console = new StringWriter();
        Main.setConsole(console);

        Main obj = new Main();
        String errorMsg = "errorMsg";
        SMTPClientException e = new SMTPClientException(errorMsg);
        obj.processErrorMessage(e);

        String consoleString = console.toString();
        assertThat(consoleString, containsString(errorMsg));
    }

    @Test
    public void testProcessInputCommand() throws Exception {
        StringWriter network = new StringWriter();
        Main.setNetwork(network);

        Main obj = new Main();
        String[] args = new String[2];
        args[0] = "joe@example.com";
        args[1] = "Hi there!";
        obj.buildParameterMap(args);
        obj.processInputCommand();

        String consoleString = network.toString();
        assertThat(consoleString, containsString("connect smtp\nTo: joe@example.com\n\nHi there!\n\ndisconnect"));
    }

    @Test
    public void testValidateInputParameters_emptyMessageBody_expectException() throws Exception {
        expectedException.expect(SMTPClientException.class);
        expectedException.expectMessage(SMTPClientConstants.INVALID_MESSAGE_BODY);

        Main obj = new Main();
        String[] args = new String[2];
        args[0] = "joe@example.com";
        args[1] = "";

        obj.buildParameterMap(args);
        obj.validateInputParameters();
    }

    @Test
    public void testValidateInputParameters_nullMessageBody_expectException() throws Exception {
        StringWriter network = new StringWriter();
        Main.setNetwork(network);

        expectedException.expect(SMTPClientException.class);
        expectedException.expectMessage(SMTPClientConstants.INVALID_MESSAGE_BODY);

        Main obj = new Main();
        String[] args = new String[2];
        args[0] = "joe@example.com";
        args[1] = null;

        obj.buildParameterMap(args);
        obj.validateInputParameters();
    }

    @Test
    public void testValidateParameters_multipleInvalidEmailIds_expectException() throws Exception {
        expectedException.expect(SMTPClientException.class);
        expectedException.expectMessage(SMTPClientConstants.INVALID_EMAIL_ADDRESSES);

        Main obj = new Main();
        String[] args = new String[2];
        args[0] = "joeexample.com,jeanexample.com";
        args[1] = "message body!";

        obj.buildParameterMap(args);
        obj.validateInputParameters();
    }
}
