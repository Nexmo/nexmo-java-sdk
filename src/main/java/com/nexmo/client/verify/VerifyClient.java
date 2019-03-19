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

import com.nexmo.client.AbstractClient;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;

import java.io.IOException;
import java.util.Locale;

/**
 * A client for talking to the Nexmo Verify API. The standard way to obtain an instance of this class is to use
 * {@link NexmoClient#getVerifyClient()}.
 * <p>
 * Send a verification request with a call to {@link #verify}, confirm the code entered by the user with
 * {@link #check}, and search in-progress or completed verification requests with {@link #search}
 * <p>
 * More information on method parameters can be found at Nexmo website:
 * <a href="https://docs.nexmo.com/verify">https://docs.nexmo.com/verify</a>
 */
public class VerifyClient extends AbstractClient {

    private CheckEndpoint check;
    private VerifyEndpoint verify;
    private SearchEndpoint search;
    private ControlEndpoint control;

    /**
     * Constructor.
     *
     * @param httpWrapper (required) shared HTTP wrapper object used for making REST calls.
     */
    public VerifyClient(HttpWrapper httpWrapper) {
        super(httpWrapper);

        this.check = new CheckEndpoint(httpWrapper);
        this.search = new SearchEndpoint(httpWrapper);
        this.verify = new VerifyEndpoint(httpWrapper);
        this.control = new ControlEndpoint(httpWrapper);
    }

    /**
     * Send a verification request to a phone number.
     *
     * @param number (required) The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a>
     *               format.
     * @param brand  (required) The name of the company or app to be verified for. Must not be longer than 18
     *               characters.
     * @return a VerifyResponse representing the response received from the Verify API call.
     * @throws IOException          if a network error occurred contacting the Nexmo Verify API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public VerifyResponse verify(final String number, final String brand) throws IOException, NexmoClientException {
        return this.verify.verify(number, brand);
    }

    /**
     * Send a verification request to a phone number.
     *
     * @param number (required) The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a>
     *               format.
     * @param brand  (required) The name of the company or app to be verified for. Must not be longer than 18
     *               characters.
     * @param from   (optional The Nexmo number to use as the sender for the verification SMS message and calls, in
     *               <a href="https://en.wikipedia.org/wiki/E.164">E.164</a> format.
     * @return a VerifyResponse representing the response received from the Verify API call.
     * @throws IOException          if a network error occurred contacting the Nexmo Verify API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public VerifyResponse verify(final String number,
                               final String brand,
                               final String from) throws IOException, NexmoClientException {
        return this.verify.verify(number, brand, from);
    }

    /**
     * Send a verification request to a phone number.
     *
     * @param number (required) The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a>
     *               format.
     * @param brand  (required) The name of the company or app to be verified for. Must not be longer than 18
     *               characters.
     * @param from   (optional The Nexmo number to use as the sender for the verification SMS message and calls, in
     *               <a href="https://en.wikipedia.org/wiki/E.164">E.164</a> format.
     * @param length (optional) The length of the verification code to be sent to the user. Must be either 4 or 6. Use
     *               -1 to use the default value.
     * @param locale (optional) Override the default locale used for verification. By default the locale is determined
     *               from the country code included in {@code number}
     * @return a VerifyResponse representing the response received from the Verify API call.
     * @throws IOException          if a network error occurred contacting the Nexmo Verify API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public VerifyResponse verify(final String number,
                               final String brand,
                               final String from,
                               final int length,
                               final Locale locale) throws IOException, NexmoClientException {
        return this.verify.verify(number, brand, from, length, locale);
    }

    /**
     * Send a verification request to a phone number.
     *
     * @param number (required) The recipient's phone number in <a href="https://en.wikipedia.org/wiki/E.164">E.164</a>
     *               format.
     * @param brand  (required) The name of the company or app to be verified for. Must not be longer than 18
     *               characters.
     * @param from   (optional The Nexmo number to use as the sender for the verification SMS message and calls, in
     *               <a href="https://en.wikipedia.org/wiki/E.164">E.164</a> format.
     * @param length (optional) The length of the verification code to be sent to the user. Must be either 4 or 6. Use
     *               -1 to use the default value.
     * @param locale (optional) Override the default locale used for verification. By default the locale is determined
     *               from the country code included in {@code number}
     * @param type   (optional) If provided, restrict the verification to the specified network type. Contact
     *               support@nexmo.com to enable this feature.
     * @return a VerifyResponse representing the response received from the Verify API call.
     * @throws IOException          if a network error occurred contacting the Nexmo Verify API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public VerifyResponse verify(final String number,
                               final String brand,
                               final String from,
                               final int length,
                               final Locale locale,
                               final VerifyRequest.LineType type) throws IOException, NexmoClientException {
        return this.verify.verify(number, brand, from, length, locale, type);
    }

    /**
     * Send a verification request to a phone number.
     */
    public VerifyResponse verify(VerifyRequest request) throws IOException, NexmoClientException {
        return this.verify.verify(request);
    }

    /**
     * Validate a code provided by a user in response to a call from {@link #verify}.
     *
     * @param requestId (required) The requestId returned by the {@code verify} call.
     * @param code      (required) The code entered by the user.
     * @return a CheckResponse representing the response received from the API call.
     * @throws IOException          if a network error occurred contacting the Nexmo Verify API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public CheckResponse check(final String requestId, final String code) throws IOException, NexmoClientException {
        return this.check.check(requestId, code);
    }

    /**
     * Validate a code provided by a user in response to a call from {@link #verify}.
     *
     * @param requestId (required) The requestId returned by the {@code verify} call.
     * @param code      (required) The code entered by the user.
     * @param ipAddress (optional) The IP address obtained from the HTTP request made when the user entered their code.
     * @return a CheckResponse representing the response received from the API call.
     * @throws IOException          if a network error occurred contacting the Nexmo Verify API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public CheckResponse check(final String requestId,
                             final String code,
                             final String ipAddress) throws IOException, NexmoClientException {
        return this.check.check(requestId, code, ipAddress);
    }

    /**
     * Search for a previous verification request.
     *
     * @param requestId The requestId of a single Verify request to be looked up.
     * @return A SearchVerifyResponse containing the details of the Verify request that was looked up, or {@code null} if no
     * record was found.
     * @throws IOException          if a network error occurred contacting the Nexmo Verify API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public SearchVerifyResponse search(String requestId) throws IOException, NexmoClientException {
        return this.search.search(requestId);
    }

    /**
     * Search for a previous verification request.
     *
     * @param requestIds The requestIds of Verify requests to be looked up.
     * @return An array SearchVerifyResponse for each record that was found.
     * @throws IOException          if a network error occurred contacting the Nexmo Verify API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public SearchVerifyResponse search(String... requestIds) throws IOException, NexmoClientException {
        return this.search.search(requestIds);
    }

    /**
     * Advance a current verification request to the next stage in the process.
     *
     * @param requestId The requestId of the ongoing verification request.
     * @return A {@link ControlResponse} representing the response from the API.
     * @throws IOException          If an IO error occurred while making the request.
     * @throws NexmoClientException If the request failed for some reason.
     */
    public ControlResponse advanceVerification(String requestId) throws IOException, NexmoClientException {
        return this.control.execute(new ControlRequest(requestId, VerifyControlCommand.TRIGGER_NEXT_EVENT));
    }

    /**
     * Cancel a current verification request.
     *
     * @param requestId The requestId of the ongoing verification request.
     * @return A {@link ControlResponse} representing the response from the API.
     * @throws IOException          If an IO error occurred while making the request.
     * @throws NexmoClientException If the request failed for some reason.
     */
    public ControlResponse cancelVerification(String requestId) throws IOException, NexmoClientException {
        return this.control.execute(new ControlRequest(requestId, VerifyControlCommand.CANCEL));
    }
}
