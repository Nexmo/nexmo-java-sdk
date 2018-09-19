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
package com.nexmo.client.insight.advanced;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.insight.CallerType;
import com.nexmo.client.insight.RoamingDetails;
import com.nexmo.client.insight.standard.StandardInsightResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdvancedInsightResponse extends StandardInsightResponse {
    private Validity validNumber;
    private Reachability reachability;
    private PortedStatus ported;
    private Integer lookupOutcome;
    private String lookupOutcomeMessage;
    private RoamingDetails roaming;
    private String callerName;
    private String firstName;
    private String lastName;
    private CallerType callerType;


    public static AdvancedInsightResponse fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, AdvancedInsightResponse.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce AdvancedInsightResponse from json.", jpe);
        }
    }

    @JsonProperty("valid_number")
    public Validity getValidNumber() {
        return validNumber;
    }

    @JsonProperty("reachable")
    public Reachability getReachability() {
        return reachability;
    }

    public PortedStatus getPorted() {
        return ported;
    }

    @JsonProperty("lookup_outcome")
    public Integer getLookupOutcome() {
        return lookupOutcome;
    }

    @JsonProperty("lookup_outcome_message")
    public String getLookupOutcomeMessage() {
        return lookupOutcomeMessage;
    }

    public RoamingDetails getRoaming() {
        return roaming;
    }

    @JsonProperty("caller_name")
    public String getCallerName() {
        return callerName;
    }

    @JsonProperty("first_name")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("last_name")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("caller_type")
    public CallerType getCallerType() {
        return callerType;
    }

    public enum PortedStatus {
        UNKNOWN, PORTED, NOT_PORTED, ASSUMED_NOT_PORTED, ASSUMED_PORTED;

        private static final Map<String, PortedStatus> PORTED_STATUS_INDEX = new HashMap<>();

        static {
            for (PortedStatus portedStatus : PortedStatus.values()) {
                PORTED_STATUS_INDEX.put(portedStatus.name(), portedStatus);
            }
        }

        @JsonCreator
        public static PortedStatus fromString(String name) {
            PortedStatus foundPortedStatus = PORTED_STATUS_INDEX.get(name.toUpperCase());
            return (foundPortedStatus != null) ? foundPortedStatus : UNKNOWN;
        }

    }

    public enum Validity {
        UNKNOWN, VALID, NOT_VALID;

        private static final Map<String, Validity> VALIDITY_INDEX = new HashMap<>();

        static {
            for (Validity validity : Validity.values()) {
                VALIDITY_INDEX.put(validity.name(), validity);
            }
        }

        @JsonCreator
        public static Validity fromString(String name) {
            Validity foundValidity = VALIDITY_INDEX.get(name.toUpperCase());
            return (foundValidity != null) ? foundValidity : Validity.UNKNOWN;
        }
    }

    public enum Reachability {
        UNKNOWN, REACHABLE, UNDELIVERABLE, ABSENT, BAD_NUMBER, BLACKLISTED;

        private static final Map<String, Reachability> REACHABILITY_INDEX = new HashMap<>();

        static {
            for (Reachability reachability : Reachability.values()) {
                REACHABILITY_INDEX.put(reachability.name(), reachability);
            }
        }

        @JsonCreator
        public static Reachability fromString(String name) {
            Reachability foundReachability = REACHABILITY_INDEX.get(name.toUpperCase());
            return (foundReachability != null) ? foundReachability : UNKNOWN;
        }
    }
}
