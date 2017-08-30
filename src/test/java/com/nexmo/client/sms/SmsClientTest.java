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
package com.nexmo.client.sms;


import com.nexmo.client.HttpWrapper;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.sms.messages.Message;
import com.nexmo.client.sms.messages.TextMessage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SmsClientTest {
    private HttpWrapper wrapper;
    private SmsClient client;

    @Before
    public void setUp() throws ParserConfigurationException {
        wrapper = new HttpWrapper(new TokenAuthMethod("not-an-api-key", "secret"));
        client = new SmsClient(wrapper);
    }

    private HttpClient stubHttpClient(int statusCode, String content) throws Exception {
        HttpClient result = mock(HttpClient.class);

        HttpResponse response = mock(HttpResponse.class);
        StatusLine sl = mock(StatusLine.class);
        HttpEntity entity = mock(HttpEntity.class);

        when(result.execute(any(HttpUriRequest.class))).thenReturn(response);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes("UTF-8")));
        when(sl.getStatusCode()).thenReturn(statusCode);
        when(response.getStatusLine()).thenReturn(sl);
        when(response.getEntity()).thenReturn(entity);

        return result;
    }

    @Test
    public void testSubmitMessage() throws Exception {
        this.wrapper.setHttpClient(this.stubHttpClient(
                200,
                "<?xml version='1.0' encoding='UTF-8' ?>\n" +
                        "<mt-submission-response>\n" +
                        "    <messages count='2'>\n" +
                        "        <message>\n" +
                        "            <to>not-a-number</to>\n" +
                        "            <messageId>message-id-1</messageId>\n" +
                        "            <status>0</status>\n" +
                        "            <remainingBalance>26.43133450</remainingBalance>\n" +
                        "            <messagePrice>0.03330000</messagePrice>\n" +
                        "            <network>12345</network>\n" +
                        "        </message>\n" +
                        "        <message>\n" +
                        "            <to>not-a-number</to>\n" +
                        "            <messageId>message-id-2</messageId>\n" +
                        "            <status>0</status>\n" +
                        "            <remainingBalance>26.39803450</remainingBalance>\n" +
                        "            <messagePrice>0.03330000</messagePrice>\n" +
                        "            <network>12345</network>\n" +
                        "        </message>\n" +
                        "    </messages>\n" +
                        "</mt-submission-response>"));

        Message message = new TextMessage("TestSender", "not-a-number", "Test");

        SmsSubmissionResult[] r = client.submitMessage(message);
        assertEquals(r.length, 2);
    }

    @Test
    public void testSubmitMessageHttpError() throws Exception {
        this.wrapper.setHttpClient(this.stubHttpClient(500, ""));

        Message message = new TextMessage("TestSender", "not-a-number", "Test");
        try {
            SmsSubmissionResult[] r = client.submitMessage(message);
            fail("An IOException should be thrown if an HTTP 500 response is received.");
        } catch (IOException ioe) {
            // This is expected
        }
    }

    @Test
    public void testSetBaseUri() throws Exception {
        assertNull(this.client.getBaseUri());
        this.client.setBaseUri("https://example.com");
        assertEquals("https://example.com", this.client.getBaseUri());
    }

    @Test
    public void testConstructBaseUri() throws Exception {
        SmsClient smsClientWithBaseUri = new SmsClient(wrapper, "https://example.com");
        assertEquals("https://example.com", smsClientWithBaseUri.getBaseUri());
    }
}
