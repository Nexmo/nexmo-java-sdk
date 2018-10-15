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
package com.nexmo.client.verify;

import com.nexmo.client.HttpConfig;
import com.nexmo.client.HttpWrapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class VerifyMethodTest extends MethodTest<VerifyMethod> {

    @Before
    public void setUp() {
        method = new VerifyMethod(new HttpWrapper());
    }

    @Test
    public void testConstructVerifyParams() throws Exception {
        VerifyRequest verifyRequest = new VerifyRequest("4477990090090",
                "Brand.com",
                "Your friend",
                4,
                new Locale("en", "GB"),
                VerifyRequest.LineType.MOBILE
        );

        RequestBuilder request = method.makeRequest(verifyRequest);
        List<NameValuePair> params = request.getParameters();

        assertContainsParam(params, "number", "4477990090090");
        assertContainsParam(params, "brand", "Brand.com");
        assertContainsParam(params, "sender_id", "Your friend");
        assertContainsParam(params, "code_length", "4");
        assertContainsParam(params, "lg", "en-gb");
        assertContainsParam(params, "require_type", "MOBILE");
    }

    @Test
    public void testConstructVerifyParamsMissingValues() throws Exception {
        VerifyRequest verifyRequest = new VerifyRequest("4477990090090", "Brand.com", null, 0, null, null);

        RequestBuilder request = method.makeRequest(verifyRequest);
        List<NameValuePair> params = request.getParameters();
        assertContainsParam(params, "number", "4477990090090");
        assertContainsParam(params, "brand", "Brand.com");
        assertParamMissing(params, "code_length");
        assertParamMissing(params, "lg");
        assertParamMissing(params, "sender_id");
        assertParamMissing(params, "require_type");
        assertParamMissing(params, "country");
        assertParamMissing(params, "pin_expiry");
        assertParamMissing(params, "next_event_wait");
    }

    @Test
    public void testConstructVerifyParamsWithOptionalValues() throws Exception {
        VerifyRequest verifyRequest = new VerifyRequest("4477990090090", "Brand.com");
        verifyRequest.setFrom("VERIFICATION");
        verifyRequest.setLength(6);
        verifyRequest.setLocale(new Locale("en", "gb"));
        verifyRequest.setType(VerifyRequest.LineType.LANDLINE);
        verifyRequest.setCountry("ZZ");
        verifyRequest.setPinExpiry(60);
        verifyRequest.setNextEventWait(90);

        RequestBuilder request = method.makeRequest(verifyRequest);
        List<NameValuePair> params = request.getParameters();
        assertContainsParam(params, "number", "4477990090090");
        assertContainsParam(params, "brand", "Brand.com");

        assertContainsParam(params, "code_length", "6");
        assertContainsParam(params, "sender_id", "VERIFICATION");
        assertContainsParam(params, "lg", "en-gb");
        assertContainsParam(params, "require_type", "LANDLINE");
        assertContainsParam(params, "country", "ZZ");
        assertContainsParam(params, "pin_expiry", "60");
        assertContainsParam(params, "next_event_wait", "90");
    }

    @Test
    public void testDefaultUri() throws Exception {
        VerifyRequest request = new VerifyRequest("4477990090090",
                "Brand.com",
                "Your friend",
                4,
                new Locale("en", "GB"),
                VerifyRequest.LineType.MOBILE
        );

        RequestBuilder builder = method.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://api.nexmo.com/verify/json",
                builder.build().getURI().toString()
        );
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(new HttpConfig.Builder().baseUri("https://example.com").build());
        VerifyMethod method = new VerifyMethod(wrapper);
        VerifyRequest request = new VerifyRequest("4477990090090",
                "Brand.com",
                "Your friend",
                4,
                new Locale("en", "GB"),
                VerifyRequest.LineType.MOBILE
        );

        RequestBuilder builder = method.makeRequest(request);
        assertEquals("POST", builder.getMethod());
        assertEquals("https://example.com/verify/json", builder.build().getURI().toString());
    }
}
