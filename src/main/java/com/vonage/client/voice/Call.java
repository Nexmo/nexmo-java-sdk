/*
 *   Copyright 2023 Vonage
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
package com.vonage.client.voice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import com.vonage.client.voice.ncco.Action;
import java.io.IOException;
import java.util.Collection;

/**
 * Call encapsulates the information required to create a call using {@link VoiceClient#createCall(Call)}
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"_links"})
public class Call {
    private Endpoint[] to;
    private Endpoint from;
    private String answerMethod = "GET", answerUrl, eventUrl, eventMethod;
    private MachineDetection machineDetection;
    private Integer lengthTimer, ringingTimer;
    private Boolean fromRandomNumber;
    private Collection<? extends Action> ncco;

    public Call() {
    }

    public Call(String to, String from, String answerUrl) {
        this(new PhoneEndpoint(to), new PhoneEndpoint(from), answerUrl);
    }

    public Call(Endpoint to, Endpoint from, String answerUrl) {
        this(new Endpoint[]{to}, from, answerUrl);
    }

    public Call(String to, String from, Collection<? extends Action> ncco) {
        this(new PhoneEndpoint(to), new PhoneEndpoint(from), ncco);
    }

    public Call(Endpoint to, Endpoint from, Collection<? extends Action> ncco) {
        this(new Endpoint[]{to}, from, ncco);
    }

    public Call(Endpoint[] to, Endpoint from, String answerUrl) {
        this.to = to;
        this.from = from;
        this.answerUrl = answerUrl;
    }

    public Call(Endpoint[] to, Endpoint from, Collection<? extends Action> ncco) {
        this.to = to;
        this.from = from;
        this.ncco = ncco;
    }

    public Endpoint[] getTo() {
        return to;
    }

    public void setTo(Endpoint[] to) {
        this.to = to;
    }

    public Endpoint getFrom() {
        return from;
    }

    public void setFrom(Endpoint from) {
        this.from = from;
    }

    @JsonProperty("answer_url")
    public String[] getAnswerUrl() {
        return (answerUrl != null) ? new String[]{answerUrl} : null;
    }

    public void setAnswerUrl(String answerUrl) {
        this.answerUrl = answerUrl;
    }

    @JsonProperty("answer_method")
    public String getAnswerMethod() {
        // Hide the answer method if the answer url isn't defined
        return answerUrl != null ? answerMethod : null;
    }

    public void setAnswerMethod(String answerMethod) {
        this.answerMethod = answerMethod;
    }

    @JsonProperty("event_url")
    public String[] getEventUrl() {
        if (eventUrl == null) {
            return null;
        }
        return new String[]{eventUrl};
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    @JsonProperty("event_method")
    public String getEventMethod() {
        return eventMethod;
    }

    public void setEventMethod(String eventMethod) {
        this.eventMethod = eventMethod;
    }

    @JsonProperty("machine_detection")
    public MachineDetection getMachineDetection() {
        return machineDetection;
    }

    public void setMachineDetection(MachineDetection machineDetection) {
        this.machineDetection = machineDetection;
    }

    @JsonProperty("length_timer")
    public Integer getLengthTimer() {
        return lengthTimer;
    }

    public void setLengthTimer(Integer lengthTimer) {
        this.lengthTimer = lengthTimer;
    }

    @JsonProperty("ringing_timer")
    public Integer getRingingTimer() {
        return ringingTimer;
    }

    public void setRingingTimer(Integer ringingTimer) {
        this.ringingTimer = ringingTimer;
    }

    @JsonProperty("from_random_number")
    public Boolean getFromRandomNumber() {
        return fromRandomNumber;
    }

    /**
     * Set to true to use random phone number as from. The number will be selected from the list of the numbers assigned to the current application. random_from_number: true cannot be used together with from.
     * @param fromRandomNumber Whether to use random number.
     */
    public void setFromRandomNumber(Boolean fromRandomNumber) {
        this.fromRandomNumber = fromRandomNumber;
    }

    @JsonProperty("ncco")
    public Collection<? extends Action> getNcco() {
        return ncco;
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException jpe) {
            throw new VonageUnexpectedException("Failed to produce json from Call object.", jpe);
        }
    }

    public static Call fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, Call.class);
        } catch (IOException jpe) {
            throw new VonageUnexpectedException("Failed to produce json from Call object.", jpe);
        }
    }
}
