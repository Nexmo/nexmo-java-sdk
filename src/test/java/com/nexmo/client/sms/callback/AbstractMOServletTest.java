/*
 * Copyright (c) 2011-2017 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.sms.callback;

import com.nexmo.client.sms.callback.messages.MO;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.concurrent.Executor;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


class SynchronousExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        command.run();
    }
}


class TestMOServlet extends AbstractMOServlet {
    public MO result;

    TestMOServlet() {
        this(false, null, false, null, null);
    }

    TestMOServlet(final boolean validateSignature,
                         final String signatureSharedSecret,
                         final boolean validateUsernamePassword,
                         final String expectedUsername,
                         final String expectedPassword) {
        super(validateSignature, signatureSharedSecret, validateUsernamePassword, expectedUsername, expectedPassword);
        this.consumer = new SynchronousExecutor();
    }

    @Override
    public void consume(MO mo) {
        this.result = mo;
    }
}


public class AbstractMOServletTest {
    @Test
    public void testHandleEmptyRequest() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        Enumeration<String> paramNames = mock(Enumeration.class);
        when(paramNames.hasMoreElements()).thenReturn(false);
        when(request.getParameterNames()).thenReturn(paramNames);

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestMOServlet().doPost(request, response);
        verify(response, atLeastOnce()).setStatus(200);
        assertEquals("OK", dummyResponseWriter.toString());
    }

    @Test
    public void testHandleValidTextRequest() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        TestMOServlet servlet = new TestMOServlet();
        servlet.doPost(request, response);
        assertEquals("OK", dummyResponseWriter.toString());

        MO mo = servlet.result;
        assertEquals("anisdn", mo.getSender());
        assertEquals("to", mo.getDestination());
        assertEquals("networkcode", mo.getNetworkCode());
        assertEquals("messageid", mo.getMessageId());
        assertEquals("asessionid", mo.getSessionId());
        assertEquals("akeyword", mo.getKeyword());
        assertEquals("text", mo.getMessageType().getType());
        assertEquals("Dear John", mo.getMessageBody());
        assertEquals(false, mo.isConcat());
        assertEquals(new GregorianCalendar(2016, 10, 7, 6, 5, 4).getTime(), mo.getTimeStamp());
    }

    @Test
    public void testHandleValidUnicodeRequest() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("type")).thenReturn("unicode");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        TestMOServlet servlet = new TestMOServlet();
        servlet.doPost(request, response);
        assertEquals("OK", dummyResponseWriter.toString());

        MO mo = servlet.result;
        assertEquals("anisdn", mo.getSender());
        assertEquals("to", mo.getDestination());
        assertEquals("networkcode", mo.getNetworkCode());
        assertEquals("messageid", mo.getMessageId());
        assertEquals("asessionid", mo.getSessionId());
        assertEquals("akeyword", mo.getKeyword());
        assertEquals("unicode", mo.getMessageType().getType());
        assertEquals("Dear John", mo.getMessageBody());
    }

    @Test
    public void testHandleValidBinaryRequest() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("type")).thenReturn("binary");
        when(request.getParameter("data")).thenReturn("00107F70FF");
        when(request.getParameter("price")).thenReturn("12.00");
        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        TestMOServlet servlet = new TestMOServlet();
        servlet.doPost(request, response);
        assertEquals("OK", dummyResponseWriter.toString());

        MO mo = servlet.result;
        assertEquals("anisdn", mo.getSender());
        assertEquals("to", mo.getDestination());
        assertEquals("networkcode", mo.getNetworkCode());
        assertEquals("messageid", mo.getMessageId());
        assertEquals("asessionid", mo.getSessionId());
        assertEquals("binary", mo.getMessageType().getType());
        assertArrayEquals(new byte[] {0x00, 0x10, 0x7f, 0x70, -1}, mo.getBinaryMessageBody());
    }

    @Test
    public void testHandleBinaryRequestMissingData() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("type")).thenReturn("binary");
        when(request.getParameter("data")).thenReturn(null);
        when(request.getParameter("price")).thenReturn("12.00");
        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        TestMOServlet servlet = new TestMOServlet();
        servlet.doGet(request, response);
        verify(response, atLeastOnce()).sendError(400, "Missing data field");

    }

    @Test
    public void testHandleBadPrice() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("price")).thenReturn("not a number");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestMOServlet().doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Bad price field");
    }

    @Test
    public void testHandleBadMessageType() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("type")).thenReturn("fluffy");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestMOServlet().doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Unrecognized message type: fluffy");
    }

    @Test
    public void testHandleMissingTextField() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("text")).thenReturn(null);

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestMOServlet().doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Missing text field");
    }

    @Test
    public void testHandleBadTimestamp() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("message-timestamp")).thenReturn("this is not a date-time");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestMOServlet().doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Bad message-timestamp format");
    }

    @Test
    public void testHandleValidateUsernamePassword() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("default");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        TestMOServlet servlet = new TestMOServlet(false, null, true, "admin", "default");
        servlet.doPost(request, response);
        assertEquals("OK", dummyResponseWriter.toString());

        MO mo = servlet.result;
        assertEquals("anisdn", mo.getSender());
        assertEquals("to", mo.getDestination());
        assertEquals("networkcode", mo.getNetworkCode());
        assertEquals("messageid", mo.getMessageId());
        assertEquals("asessionid", mo.getSessionId());
        assertEquals("akeyword", mo.getKeyword());
        assertEquals("text", mo.getMessageType().getType());
        assertEquals("Dear John", mo.getMessageBody());
    }

    @Test
    public void testHandleMissingUsername() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("username")).thenReturn(null);
        when(request.getParameter("password")).thenReturn("default");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestMOServlet(false, null, true, "admin", "default").doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Bad Credentials");
    }

    @Test
    public void testHandleIncorrectUsername() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("username")).thenReturn("dave");
        when(request.getParameter("password")).thenReturn("default");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestMOServlet(false, null, true, "admin", "default").doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Bad Credentials");
    }

    @Test
    public void testHandleMissingPassword() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("username")).thenReturn("dave");
        when(request.getParameter("password")).thenReturn(null);

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestMOServlet(false, null, true, "admin", "default").doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Bad Credentials");
    }

    @Test
    public void testHandleIncorrectPassword() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("username")).thenReturn("dave");
        when(request.getParameter("password")).thenReturn("password");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        new TestMOServlet(false, null, true, "admin", "default").doPost(request, response);
        verify(response, atLeastOnce()).sendError(400, "Bad Credentials");
    }

    /**
     * If validateUserCredentials is true, and the servlet is instantiated with null username and password,
     * no validation is actually done.
     */
    @Test
    public void testHandleValidateNullCredentials() throws IOException, ServletException {
        HttpServletRequest request = dummyTextRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter dummyResponseWriter = new StringWriter();

        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("password");

        when(response.getWriter()).thenReturn(new PrintWriter(dummyResponseWriter));

        TestMOServlet servlet = new TestMOServlet(false, null, true, null, null);
        servlet.doPost(request, response);
        assertEquals("OK", dummyResponseWriter.toString());

        MO mo = servlet.result;
        assertEquals("anisdn", mo.getSender());
        assertEquals("to", mo.getDestination());
        assertEquals("networkcode", mo.getNetworkCode());
        assertEquals("messageid", mo.getMessageId());
        assertEquals("asessionid", mo.getSessionId());
        assertEquals("akeyword", mo.getKeyword());
        assertEquals("text", mo.getMessageType().getType());
        assertEquals("Dear John", mo.getMessageBody());
    }

    private HttpServletRequest dummyTextRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getParameter("msisdn")).thenReturn("anisdn");
        when(request.getParameter("to")).thenReturn("to");
        when(request.getParameter("network-code")).thenReturn("networkcode");
        when(request.getParameter("messageId")).thenReturn("messageid");
        when(request.getParameter("sessionId")).thenReturn("asessionid");
        when(request.getParameter("keyword")).thenReturn("akeyword");
        when(request.getParameter("type")).thenReturn("text");
        when(request.getParameter("text")).thenReturn("Dear John");
        when(request.getParameter("message-timestamp")).thenReturn("2016-11-07 06:05:04");

        Enumeration<String> paramNames = mock(Enumeration.class);
        when(paramNames.hasMoreElements()).thenReturn(true);
        when(request.getParameterNames()).thenReturn(paramNames);

        return request;
    }
}
