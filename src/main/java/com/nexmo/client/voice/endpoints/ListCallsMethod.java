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


import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.voice.CallInfoPage;
import com.nexmo.client.voice.CallsFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;

public class ListCallsMethod extends AbstractMethod<CallsFilter, CallInfoPage> {
    private static final Log LOG = LogFactory.getLog(CreateCallMethod.class);

    private static final String PATH = "/calls";
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{JWTAuthMethod.class};
    private String uri;

    public ListCallsMethod(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(CallsFilter filter) throws NexmoClientException, UnsupportedEncodingException {
        URIBuilder uriBuilder;
        // TODO: Remove in 4.0.0 along with setUri and getUri method
        String uri = (this.uri != null) ? this.uri : httpWrapper.getHttpConfig().getVersionedApiBaseUri("v1") + PATH;

        try {
            uriBuilder = new URIBuilder(uri);
        } catch (URISyntaxException e) {
            throw new NexmoUnexpectedException("Could not parse URI: " + uri);
        }
        if (filter != null) {
            List<NameValuePair> params = filter.toUrlParams();
            for (NameValuePair param : params) {
                uriBuilder.setParameter(param.getName(), param.getValue());
            }
        }
        return RequestBuilder.get().setUri(uriBuilder.toString());
    }

    @Override
    public CallInfoPage parseResponse(HttpResponse response) throws IOException {
        String json = new BasicResponseHandler().handleResponse(response);
        return CallInfoPage.fromJson(json);
    }

    /**
     * @deprecated Use {@link com.nexmo.client.HttpConfig.Builder} to create an {@link com.nexmo.client.HttpConfig} object and pass into {@link com.nexmo.client.NexmoClient}
     */
    @Deprecated
    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return (this.uri != null) ? this.uri : httpWrapper.getHttpConfig().getApiBaseUri() + PATH;
    }
}
