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

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoBadRequestException;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.NexmoMethodFailedException;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.voice.endpoints.AbstractMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class BuyNumberEndpoint extends AbstractMethod<BuyNumberRequest, BuyNumberResponse> {
    private static final String PATH = "/number/buy";
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{TokenAuthMethod.class};

    public BuyNumberEndpoint(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(BuyNumberRequest request) throws NexmoClientException, UnsupportedEncodingException {
        RequestBuilder requestBuilder = RequestBuilder
                .post()
                .setUri(httpWrapper.getHttpConfig().getRestBaseUri() + PATH);
        request.addParams(requestBuilder);
        return requestBuilder;
    }

    @Override
    public BuyNumberResponse parseResponse(HttpResponse response) throws IOException, NexmoClientException {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 400 || statusCode == 420) {
            throw new NexmoBadRequestException(EntityUtils.toString(response.getEntity()));
        } else if (statusCode >= 500) {
            throw new NexmoMethodFailedException(EntityUtils.toString(response.getEntity()));
        }
        String json = new BasicResponseHandler().handleResponse(response);
        return BuyNumberResponse.fromJson(json);
    }


}
