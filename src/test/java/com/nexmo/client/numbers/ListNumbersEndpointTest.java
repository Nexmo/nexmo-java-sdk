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
package com.nexmo.client.numbers;

import com.nexmo.client.HttpConfig;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.TestUtils;
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
import java.util.Map;

import static com.nexmo.client.TestUtils.test429;
import static org.junit.Assert.assertEquals;


public class ListNumbersEndpointTest {
    private ListNumbersEndpoint endpoint;

    @Before
    public void setUp() throws Exception {
        this.endpoint = new ListNumbersEndpoint(new HttpWrapper());
    }

    @Test
    public void makeRequest() throws Exception {
        ListNumbersFilter filter = new ListNumbersFilter();
        filter.setIndex(10);
        filter.setSize(20);
        filter.setPattern("234");
        filter.setSearchPattern(SearchPattern.STARTS_WITH);
        RequestBuilder request = endpoint.makeRequest(filter);

        assertEquals("GET", request.getMethod());
        Map<String, String> params = TestUtils.makeParameterMap(request.getParameters());
        assertEquals("234", params.get("pattern"));
        assertEquals("0", params.get("search_pattern"));
        assertEquals("10", params.get("index"));
        assertEquals("20", params.get("size"));
    }

    @Test
    public void parseResponse() throws Exception {
        HttpResponse stubResponse = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("1.1", 1, 1),
                200,
                "OK"
        ));

        String json = "{\n" + "  \"count\": 1,\n" + "  \"numbers\": [\n" + "    {\n" + "      \"country\": \"GB\",\n"
                + "      \"msisdn\": \"447700900000\",\n" + "      \"moHttpUrl\": \"https://example.com/mo\",\n"
                + "      \"type\": \"mobile-lvn\",\n" + "      \"features\": [\n" + "        \"VOICE\",\n"
                + "        \"SMS\"\n" + "      ],\n" + "      \"voiceCallbackType\": \"app\",\n"
                + "      \"voiceCallbackValue\": \"aaaaaaaa-bbbb-cccc-dddd-0123456789ab\"\n" + "    }\n" + "  ]\n"
                + "}";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        ListNumbersResponse response = endpoint.parseResponse(stubResponse);
        assertEquals(1, response.getCount());
    }

    @Test
    public void testRequestThrottleResponse() throws Exception {
        test429(new ListNumbersEndpoint(null));
    }

    @Test
    public void testDefaultUri() throws Exception {
        ListNumbersFilter filter = new ListNumbersFilter();
        filter.setIndex(10);
        filter.setSize(20);
        filter.setPattern("234");
        filter.setSearchPattern(SearchPattern.STARTS_WITH);

        RequestBuilder builder = endpoint.makeRequest(filter);
        assertEquals("GET", builder.getMethod());
        assertEquals("https://rest.nexmo.com/account/numbers?index=10&size=20&pattern=234&search_pattern=0",
                builder.build().getURI().toString()
        );
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(new HttpConfig.Builder().baseUri("https://example.com").build());
        ListNumbersEndpoint endpoint = new ListNumbersEndpoint(wrapper);
        ListNumbersFilter filter = new ListNumbersFilter();
        filter.setIndex(10);
        filter.setSize(20);
        filter.setPattern("234");
        filter.setSearchPattern(SearchPattern.STARTS_WITH);

        RequestBuilder builder = endpoint.makeRequest(filter);
        assertEquals("GET", builder.getMethod());
        assertEquals(
                "https://example.com/account/numbers?index=10&size=20&pattern=234&search_pattern=0",
                builder.build().getURI().toString()
        );
    }
}
