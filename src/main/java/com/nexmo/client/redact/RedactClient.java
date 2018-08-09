/*
 * Copyright (c) 2011-2018 Nexmo Inc
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
package com.nexmo.client.redact;

import com.nexmo.client.AbstractClient;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;

import java.io.IOException;

/**
 * A client for talking to the Nexmo Redact API. The standard way to obtain an instance of this class is to use
 * {@link NexmoClient#getRedactClient()}.
 */
public class RedactClient extends AbstractClient {
    private RedactEndpoint redactEndpoint;

    public RedactClient(HttpWrapper httpWrapper) {
        super(httpWrapper);

        this.redactEndpoint = new RedactEndpoint(httpWrapper);
    }

    /**
     * Submit a request to the Redact API to redact a transaction.
     *
     * @param id      The transaction id to redact.
     * @param product The {@link com.nexmo.client.redact.RedactRequest.Product} which corresponds to the transaction.
     *
     * @throws IOException          if a network error occurred contacting the Nexmo Redact API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public void redactTransaction(String id, RedactRequest.Product product) throws IOException, NexmoClientException {
        this.redactTransaction(new RedactRequest(id, product));
    }

    /**
     * Submit a request to the Redact API to redact a transaction.
     *
     * @param id      The transaction id to redact.
     * @param product The {@link com.nexmo.client.redact.RedactRequest.Product} which corresponds to the transaction.
     * @param type    The {@link com.nexmo.client.redact.RedactRequest.Type} which is required if redacting SMS data.
     *
     * @throws IOException          if a network error occurred contacting the Nexmo Redact API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public void redactTransaction(String id, RedactRequest.Product product, RedactRequest.Type type) throws IOException, NexmoClientException {
        RedactRequest request = new RedactRequest(id, product);
        request.setType(type);

        this.redactTransaction(request);
    }

    /**
     * Submit a request to the Redact API to redact a transaction.
     *
     * @param redactRequest a {@link RedactRequest} object which contains the request parameters.
     *
     * @throws IOException          if a network error occurred contacting the Nexmo Redact API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public void redactTransaction(RedactRequest redactRequest) throws IOException, NexmoClientException {
        this.redactEndpoint.redactTransaction(redactRequest);
    }
}
