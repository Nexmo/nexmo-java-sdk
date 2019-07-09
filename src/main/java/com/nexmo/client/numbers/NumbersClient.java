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
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.NexmoResponseParseException;

/**
 * A client for accessing the Nexmo API calls that manage phone numbers.
 */
public class NumbersClient {
    private ListNumbersEndpoint listNumbers;
    private SearchNumbersEndpoint searchNumbers;
    private CancelNumberEndpoint cancelNumber;
    private BuyNumberEndpoint buyNumber;
    private UpdateNumberEndpoint updateNumber;

    public NumbersClient(HttpWrapper httpWrapper) {
        this.listNumbers = new ListNumbersEndpoint(httpWrapper);
        this.searchNumbers = new SearchNumbersEndpoint(httpWrapper);
        this.cancelNumber = new CancelNumberEndpoint(httpWrapper);
        this.buyNumber = new BuyNumberEndpoint(httpWrapper);
        this.updateNumber = new UpdateNumberEndpoint(httpWrapper);
    }

    /**
     * Get the first page of phone numbers assigned to the authenticated account.
     *
     * @return A ListNumbersResponse containing the first 10 phone numbers
     *
     * @throws NexmoResponseParseException if the response from the API could not be parsed.
     * @throws NexmoClientException        if an error is returned by the server.
     */
    public ListNumbersResponse listNumbers() throws NexmoResponseParseException, NexmoClientException {
        return this.listNumbers.listNumbers(new ListNumbersFilter());
    }

    /**
     * Get a filtered set of numbers assigned to the authenticated account.
     *
     * @param filter A ListNumbersFilter describing the filters to be applied to the request.
     *
     * @return A ListNumbersResponse containing phone numbers matching the supplied filter.
     *
     * @throws NexmoResponseParseException if the response from the API could not be parsed.
     * @throws NexmoClientException        if an error is returned by the server.
     */
    public ListNumbersResponse listNumbers(ListNumbersFilter filter) throws NexmoResponseParseException, NexmoClientException {
        return this.listNumbers.listNumbers(filter);
    }


    /**
     * Search for available Nexmo Virtual Numbers.
     *
     * @throws NexmoResponseParseException if the response from the API could not be parsed.
     * @throws NexmoClientException        if an error is returned by the server.
     */
    public SearchNumbersResponse searchNumbers(String country) throws NexmoResponseParseException, NexmoClientException {
        return this.searchNumbers(new SearchNumbersFilter(country));
    }

    /**
     * Search for available Nexmo Virtual Numbers.
     *
     * @throws NexmoResponseParseException if the response from the API could not be parsed.
     * @throws NexmoClientException        if an error is returned by the server.
     */
    public SearchNumbersResponse searchNumbers(SearchNumbersFilter filter) throws NexmoResponseParseException, NexmoClientException {
        return this.searchNumbers.searchNumbers(filter);
    }

    /**
     * Start renting a Nexmo Virtual Number.
     *
     * @param country A String containing a 2-character ISO country code.
     * @param msisdn  The phone number to be bought.
     *
     * @throws NexmoResponseParseException if the response from the API could not be parsed.
     * @throws NexmoClientException        if an error is returned by the server.
     */
    public void buyNumber(String country, String msisdn) throws NexmoResponseParseException, NexmoClientException {
        this.buyNumber.execute(new BuyNumberRequest(country, msisdn));
    }

    /**
     * Stop renting a Nexmo Virtual Number.
     *
     * @param country A String containing a 2-character ISO country code.
     * @param msisdn  The phone number to be cancelled.
     *
     * @throws NexmoResponseParseException if the response from the API could not be parsed.
     * @throws NexmoClientException        if an error is returned by the server.
     */
    public void cancelNumber(String country, String msisdn) throws NexmoResponseParseException, NexmoClientException {
        this.cancelNumber.execute(new CancelNumberRequest(country, msisdn));
    }

    /**
     * Update the callbacks and/or application associations for a given Nexmo Virtual Number.
     *
     * @param request Details of the updates to be made to the number association.
     *
     * @throws NexmoResponseParseException if the response from the API could not be parsed.
     * @throws NexmoClientException        if an error is returned by the server.
     */
    public void updateNumber(UpdateNumberRequest request) throws NexmoResponseParseException, NexmoClientException {
        this.updateNumber.execute(request);
    }

    /**
     * Link a given Nexmo Virtual Number to a Nexmo Application with the given ID.
     *
     * @param msisdn  The Nexmo Virtual Number to be updated.
     * @param country The country for the given msisdn.
     * @param appId   The ID for the Nexmo Application to be associated with the number.
     *
     * @throws NexmoResponseParseException if the response from the API could not be parsed.
     * @throws NexmoClientException        if an error is returned by the server.
     */
    public void linkNumber(String msisdn, String country, String appId) throws NexmoResponseParseException, NexmoClientException {
        UpdateNumberRequest request = new UpdateNumberRequest(msisdn, country);
        request.setVoiceCallbackType(UpdateNumberRequest.CallbackType.APP);
        request.setVoiceCallbackValue(appId);
        this.updateNumber(request);
    }
}
