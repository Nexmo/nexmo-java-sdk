/*
 *   Copyright 2025 Vonage
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.vonage.client.voice.ncco;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.voice.AdvancedMachineDetection;
import com.vonage.client.voice.MachineDetection;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

/**
 * An NCCO connect action that allows for the establishment of a connection to various {@link Endpoint}.
 */
public class ConnectAction extends JsonableBaseObject implements Action {
    private static final String ACTION = "connect";

    private Collection<Endpoint> endpoint;
    private String from;
    private EventType eventType;
    private Integer limit, timeOut;
    private MachineDetection machineDetection;
    private AdvancedMachineDetection advancedMachineDetection;
    private Collection<String> eventUrl;
    private EventMethod eventMethod;
    private Boolean randomFromNumber;
    private URI ringbackTone;

    ConnectAction() {}

    private ConnectAction(Builder builder) {
        if ((endpoint = builder.endpoint) == null || endpoint.isEmpty()) {
            throw new IllegalStateException("An endpoint must be specified.");
        }
        if ((limit = builder.limit) != null && (limit < 1 || limit > 7200)) {
            throw new IllegalArgumentException("'limit' must be positive and less than 7200 seconds.");
        }
        if ((timeOut = builder.timeOut) != null && (timeOut < 3 || timeOut > 7200)) {
            throw new IllegalArgumentException("'timeOut' must be between 3 and 7200 seconds.");
        }
        from = builder.from;
        if ((randomFromNumber = builder.randomFromNumber) != null && from != null) {
            throw new IllegalStateException("'randomFromNumber' and 'from' cannot be used together.");
        }
        eventType = builder.eventType;
        machineDetection = builder.machineDetection;
        advancedMachineDetection = builder.advancedMachineDetection;
        eventUrl = builder.eventUrl;
        eventMethod = builder.eventMethod;
        ringbackTone = builder.ringbackTone;
    }

    @JsonProperty("action")
    @Override
    public String getAction() {
        return ACTION;
    }

    @JsonProperty("endpoint")
    public Collection<Endpoint> getEndpoint() {
        return endpoint;
    }

    @JsonProperty("from")
    public String getFrom() {
        return from;
    }

    @JsonProperty("eventType")
    public EventType getEventType() {
        return eventType;
    }

    @JsonProperty("timeout")
    public Integer getTimeOut() {
        return timeOut;
    }

    @JsonProperty("limit")
    public Integer getLimit() {
        return limit;
    }

    @JsonProperty("machineDetection")
    public MachineDetection getMachineDetection() {
        return machineDetection;
    }

    @JsonProperty("advancedMachineDetection")
    public AdvancedMachineDetection getAdvancedMachineDetection() {
        return advancedMachineDetection;
    }

    @JsonProperty("eventUrl")
    public Collection<String> getEventUrl() {
        return eventUrl;
    }

    @JsonProperty("eventMethod")
    public EventMethod getEventMethod() {
        return eventMethod;
    }

    @JsonProperty("randomFromNumber")
    public Boolean getRandomFromNumber() {
        return randomFromNumber;
    }

    @JsonProperty("ringbackTone")
    public URI getRingbackTone() {
        return ringbackTone;
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @param endpoint Connect the call to a specific #{@link Endpoint}.
     *
     * @return A new Builder.
     */
    public static Builder builder(Collection<Endpoint> endpoint) {
        return new Builder(endpoint);
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @param endpoint Connect the call to a specific {@linkplain Endpoint}.
     *
     * @return A new Builder.
     */
    public static Builder builder(Endpoint... endpoint) {
        return builder(Arrays.asList(endpoint));
    }

    public static class Builder {
        private Collection<Endpoint> endpoint;
        private String from;
        private EventType eventType;
        private Integer timeOut, limit;
        private MachineDetection machineDetection;
        private AdvancedMachineDetection advancedMachineDetection;
        private Collection<String> eventUrl;
        private EventMethod eventMethod;
        private Boolean randomFromNumber;
        private URI ringbackTone;

        private Builder(Collection<Endpoint> endpoint) {
            this.endpoint = endpoint;
        }

        /**
         * Connect the call to a specific {@linkplain Endpoint}.
         *
         * @param endpoint The endpoints to connect to.
         *
         * @return This builder.
         * @deprecated This will be removed in the next major release.
         */
        @Deprecated
        public Builder endpoint(Collection<Endpoint> endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        /**
         * Connect the call to a specific {@linkplain Endpoint}.
         *
         * @param endpoint The endpoint(s) to connect to.
         *
         * @return This builder.
         */
        public Builder endpoint(Endpoint... endpoint) {
            return endpoint(Arrays.asList(endpoint));
        }

        /**
         * Sets the caller ID number. This must be one of your Vonage virtual numbers.
         * Any other value will result in the caller ID being unknown.
         *
         * @param from The caller number in <a href="https://en.wikipedia.org/wiki/E.164">E.164 format</a>.
         *
         * @return This builder.
         */
        public Builder from(String from) {
            this.from = from;
            return this;
        }

        /**
         * Set to {@link EventType#SYNCHRONOUS} to:
         * <ul>
         * <li>Make the connect action synchronous.
         * <li>Enable eventUrl to return an NCCO that overrides the current NCCO when a call moves to
         * specific states.
         * </ul>
         * <p>
         * See the <a href="https://developer.vonage.com/voice/voice-api/ncco-reference#connect-with-fallback-ncco">Connect with fallback NCCO example.</a>
         *
         * @param eventType The event type as an enum.
         *
         * @return This builder.
         */
        public Builder eventType(EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        /**
         * If the call is unanswered, set the number in seconds before Vonage stops ringing endpoint.
         * The default value is 60, minimum is 3 and maximum is 7200 (2 hours).
         *
         * @param timeOut The call timeout in seconds.
         *
         * @return This builder.
         */
        public Builder timeOut(Integer timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        /**
         * Maximum length of the call in seconds. The default and maximum value is 7200 seconds (2 hours).
         *
         * @param limit The maximum call length as an int.
         *
         * @return This builder.
         */
        public Builder limit(Integer limit) {
            this.limit = limit;
            return this;
        }

        /**
         * Configure the behavior when Vonage detects that a destination is an answerphone.
         *
         * @param machineDetection
         *                         Set to either:
         *                         <ul>
         *                         <li> {@link MachineDetection#CONTINUE} Vonage sends an HTTP request to event_url with the Call event machine
         *                         <li> {@link MachineDetection#HANGUP} to end the Call
         *                         </ul>
         *
         * @return This builder.
         */
        public Builder machineDetection(MachineDetection machineDetection) {
            this.machineDetection = machineDetection;
            return this;
        }

        /**
         * Configure the behavior of Vonage's advanced machine detection. This overrides the
         * {@link #machineDetection(MachineDetection)}, so you cannot set both.
         *
         * @param advancedMachineDetection The advanced machine detection settings.
         *
         * @return This builder.
         *
         * @since 7.4.0
         */
        public Builder advancedMachineDetection(AdvancedMachineDetection advancedMachineDetection) {
            this.advancedMachineDetection = advancedMachineDetection;
            return this;
        }

        /**
         * Set the webhook endpoint that Vonage calls asynchronously on each of the possible
         * <a href="https://developer.nexmo.com/voice/voice-api/guides/call-flow#call-states">Call States</a>.
         * If eventType is set to synchronous the eventUrl can return an NCCO that overrides the current
         * NCCO when a timeout occurs.
         *
         * @param eventUrl The event URLs.
         *
         * @return This builder.
         */
        public Builder eventUrl(Collection<String> eventUrl) {
            this.eventUrl = eventUrl;
            return this;
        }

        /**
         * Set the webhook endpoint that Vonage calls asynchronously on each of the possible
         * <a href="https://developer.nexmo.com/voice/voice-api/guides/call-flow#call-states">Call States</a>.
         * If eventType is set to synchronous the eventUrl can return an NCCO that overrides the current
         * NCCO when a timeout occurs.
         *
         * @param eventUrl The event URL(s).
         *
         * @return This builder.
         */
        public Builder eventUrl(String... eventUrl) {
            return eventUrl(Arrays.asList(eventUrl));
        }

        /**
         * The HTTP method Vonage uses to make the request to eventUrl.
         * The default value is {@linkplain EventMethod#POST}.
         *
         * @param eventMethod The HTTP method as an enum.
         *
         * @return This builder.
         */
        public Builder eventMethod(EventMethod eventMethod) {
            this.eventMethod = eventMethod;
            return this;
        }

        /**
         * Use a random phone number as {@code from}. The number will be selected from the list of the
         * numbers assigned to the current application. The application will try to use number(s) from the
         * same country as the destination (if available). If set to {@code true}, cannot be used together
         * with {@linkplain #from(String)}. The default value is {@code false}.
         *
         * @param randomFromNumber {@code true} to use a random number instead of {@linkplain #from(String)}.
         *
         * @return This builder.
         * @since 8.2.0
         */
        public Builder randomFromNumber(boolean randomFromNumber) {
            this.randomFromNumber = randomFromNumber;
            return this;
        }

        /**
         * A URL value that points to a ringback tone to be played back on repeat to the caller, so they don't
         * hear silence. The tone will automatically stop playing when the call is fully connected. It's not
         * recommended to use this parameter when connecting to a phone endpoint, as the carrier will supply
         * their own ringback tone.
         *
         * @param ringbackTone The ringback tone URL as a string.
         *
         * @return This builder.
         * @since 8.2.0
         */
        public Builder ringbackTone(String ringbackTone) {
            this.ringbackTone = URI.create(ringbackTone);
            return this;
        }

        /**
         * Builds the ConnectAction.
         *
         * @return A new {@link ConnectAction} object from the stored builder options.
         */
        public ConnectAction build() {
            return new ConnectAction(this);
        }
    }
}
