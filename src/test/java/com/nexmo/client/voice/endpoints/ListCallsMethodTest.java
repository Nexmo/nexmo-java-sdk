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
package com.nexmo.client.voice.endpoints;

import com.nexmo.client.HttpConfig;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.voice.CallInfoPage;
import com.nexmo.client.voice.CallsFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.nexmo.client.TestUtils.test429;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ListCallsMethodTest {
    private static final Log LOG = LogFactory.getLog(ListCallsMethodTest.class);
    private ListCallsMethod method;

    @Before
    public void setUp() throws Exception {
        method = new ListCallsMethod(new HttpWrapper());
    }

    @Test
    public void getAcceptableAuthMethods() throws Exception {
        assertArrayEquals(new Class[]{JWTAuthMethod.class}, method.getAcceptableAuthMethods());
    }

    @Test
    public void makeRequestWithNoFilter() throws Exception {
        RequestBuilder request = method.makeRequest(null);
        assertEquals("GET", request.getMethod());
        assertEquals("https://api.nexmo.com/v1/calls", request.getUri().toString());
    }

    @Test
    public void makeRequestWithFilter() throws Exception {
        CallsFilter callsFilter = new CallsFilter();
        callsFilter.setPageSize(3);
        RequestBuilder request = method.makeRequest(callsFilter);
        assertEquals("GET", request.getMethod());
        assertEquals("https://api.nexmo.com/v1/calls?page_size=3", request.getUri().toString());
    }

    @Test
    public void parseResponse() throws Exception {
        HttpResponse stubResponse = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("1.1", 1, 1),
                200,
                "OK"
        ));

        String json = "{\n" + "  \"page_size\": 10,\n" + "  \"record_index\": 0,\n" + "  \"count\": 2,\n"
                + "  \"_embedded\": {\n" + "    \"calls\": [\n" + "      {\n"
                + "        \"uuid\": \"93137ee3-580e-45f7-a61a-e0b5716000ef\",\n"
                + "        \"status\": \"completed\",\n" + "        \"direction\": \"outbound\",\n"
                + "        \"rate\": \"0.02400000\",\n" + "        \"price\": \"0.00280000\",\n"
                + "        \"duration\": \"7\",\n" + "        \"network\": \"23410\",\n"
                + "        \"conversation_uuid\": \"aa17bd11-c895-4225-840d-30dc38c31e50\",\n"
                + "        \"start_time\": \"2017-01-13T13:55:02.000Z\",\n"
                + "        \"end_time\": \"2017-01-13T13:55:09.000Z\",\n" + "        \"to\": {\n"
                + "          \"type\": \"phone\",\n" + "          \"number\": \"447700900104\"\n" + "        },\n"
                + "        \"from\": {\n" + "          \"type\": \"phone\",\n"
                + "          \"number\": \"447700900105\"\n" + "        },\n" + "        \"_links\": {\n"
                + "          \"self\": {\n"
                + "            \"href\": \"/v1/calls/93137ee3-580e-45f7-a61a-e0b5716000ef\"\n" + "          }\n"
                + "        }\n" + "      },\n" + "      {\n"
                + "        \"uuid\": \"105e02df-940a-466c-b28b-51ae015a9166\",\n"
                + "        \"status\": \"completed\",\n" + "        \"direction\": \"outbound\",\n"
                + "        \"rate\": \"0.02400000\",\n" + "        \"price\": \"0.00280000\",\n"
                + "        \"duration\": \"7\",\n" + "        \"network\": \"23410\",\n"
                + "        \"conversation_uuid\": \"1467b438-f5a8-4937-9a65-e1f946a2f664\",\n"
                + "        \"start_time\": \"2017-01-11T15:03:46.000Z\",\n"
                + "        \"end_time\": \"2017-01-11T15:03:53.000Z\",\n" + "        \"to\": {\n"
                + "          \"type\": \"phone\",\n" + "          \"number\": \"447700900104\"\n" + "        },\n"
                + "        \"from\": {\n" + "          \"type\": \"phone\",\n"
                + "          \"number\": \"447700900105\"\n" + "        },\n" + "        \"_links\": {\n"
                + "          \"self\": {\n"
                + "            \"href\": \"/v1/calls/105e02df-940a-466c-b28b-51ae015a9166\"\n" + "          }\n"
                + "        }\n" + "      }\n" + "    ]\n" + "  },\n" + "  \"_links\": {\n" + "    \"self\": {\n"
                + "      \"href\": \"/v1/calls?page_size=10&record_index=0\"\n" + "    },\n" + "    \"first\": {\n"
                + "      \"href\": \"/v1/calls?page_size=10\"\n" + "    },\n" + "    \"last\": {\n"
                + "      \"href\": \"/v1/calls?page_size=10\"\n" + "    }\n" + "  }\n" + "}\n";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        CallInfoPage page = method.parseResponse(stubResponse);
        assertEquals(2, page.getCount());
        assertEquals(2, page.getEmbedded().getCallInfos().length);
        assertEquals("/v1/calls?page_size=10", page.getLinks().getFirst().getHref());
        assertEquals("/v1/calls?page_size=10", page.getLinks().getLast().getHref());
    }

    @Test
    public void testBadUriThrowsException() throws Exception {
        method.setUri(":this::///isnota_uri");
        assertEquals(":this::///isnota_uri", method.getUri());
        try {
            CallsFilter filter = new CallsFilter();
            filter.setPageSize(30);
            RequestBuilder request = method.makeRequest(filter);
            // Anything past here only executes if our assertion is incorrect:
            LOG.error("SnsRequest URI: " + request.getUri());
            fail("Making a request with a bad URI should throw a NexmoUnexpectedException");
        } catch (NexmoUnexpectedException nue) {
            // This is expected
        }
    }

    @Test
    public void testRequestThrottleResponse() throws Exception {
        test429(new ListCallsMethod(null));
    }

    @Test
    public void testDefaultUri() throws Exception {
        CallsFilter filter = new CallsFilter();
        filter.setPageSize(3);

        RequestBuilder builder = method.makeRequest(filter);
        assertEquals("GET", builder.getMethod());
        assertEquals("https://api.nexmo.com/v1/calls?page_size=3", builder.build().getURI().toString());
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(new HttpConfig.Builder().baseUri("https://example.com").build());
        ListCallsMethod method = new ListCallsMethod(wrapper);
        CallsFilter filter = new CallsFilter();
        filter.setPageSize(3);

        RequestBuilder builder = method.makeRequest(filter);
        assertEquals("GET", builder.getMethod());
        assertEquals("https://example.com/calls?page_size=3", builder.build().getURI().toString());
    }
}
