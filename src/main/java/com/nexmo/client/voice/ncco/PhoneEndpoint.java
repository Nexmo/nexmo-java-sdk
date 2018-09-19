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
package com.nexmo.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Represents a phone endpoint used in a {@link ConnectAction}
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PhoneEndpoint implements Endpoint {
    private static final String TYPE = "phone";

    private String number;
    private String dtmfAnswer;
    private OnAnswer onAnswer;

    private PhoneEndpoint(Builder builder) {
        this.number = builder.number;
        this.dtmfAnswer = builder.dtmfAnswer;
        this.onAnswer = (builder.onAnswer != null) ? new OnAnswer(builder.onAnswer) : null;
    }

    public String getType() {
        return TYPE;
    }

    public String getNumber() {
        return number;
    }

    public String getDtmfAnswer() {
        return dtmfAnswer;
    }

    public OnAnswer getOnAnswer() {
        return onAnswer;
    }

    public static class Builder {
        private String number;
        private String dtmfAnswer = null;
        private String onAnswer = null;

        public Builder(String number) {
            this.number = number;
        }

        public Builder number(String number) {
            this.number = number;
            return this;
        }

        public Builder dtmfAnswer(String dtmfAnswer) {
            this.dtmfAnswer = dtmfAnswer;
            return this;
        }

        public Builder onAnswer(String onAnswer) {
            this.onAnswer = onAnswer;
            return this;
        }

        public PhoneEndpoint build() {
            return new PhoneEndpoint(this);
        }
    }

    private class OnAnswer {
        private String url;

        private OnAnswer(String url) {
            this.url = url;
        }

        public String getUrl() {
            return this.url;
        }
    }
}
