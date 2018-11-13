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
package com.nexmo.client.insight;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;

import java.io.IOException;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StandardInsightResponse extends BasicInsightResponse {
    private BigDecimal requestPrice;
    private BigDecimal remainingBalance;
    private BigDecimal refundPrice;
    private CarrierDetails originalCarrier;
    private CarrierDetails currentCarrier;
    private CallerIdentity callerIdentity;

    public static StandardInsightResponse fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, StandardInsightResponse.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce StandardInsightResponse from json.", jpe);
        }
    }

    @JsonProperty("request_price")
    public BigDecimal getRequestPrice() {
        return requestPrice;
    }

    @JsonProperty("remaining_balance")
    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    @JsonProperty("original_carrier")
    public CarrierDetails getOriginalCarrier() {
        return originalCarrier;
    }

    @JsonProperty("current_carrier")
    public CarrierDetails getCurrentCarrier() {
        return currentCarrier;
    }

    @JsonProperty("caller_identity")
    public CallerIdentity getCallerIdentity() {
        return callerIdentity;
    }

    @JsonProperty("refund_price")
    public BigDecimal getRefundPrice() {
        return refundPrice;
    }
}
