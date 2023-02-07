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
package com.vonage.client.video;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.VonageUnexpectedException;
import java.io.IOException;

/**
 * Represents a Vonage Video SIP call.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class OutboundSipResponse {
    private String id, connectionId, streamId;

    protected OutboundSipResponse() {
    }

    /**
     * A unique ID for the SIP call.
     *
     * @return The unique ID of the SIP conference.
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * The video connection ID for the SIP call's connection in the video session.
     * You can use this connection ID to terminate the SIP call.
     *
     * @return The connection ID of the audio-only stream that is put into a video session.
     */
    @JsonProperty("connectionId")
    public String getConnectionId() {
        return connectionId;
    }

    /**
     * The video stream ID for the SIP call's stream in the OpenTok session.
     *
     * @return The stream ID of the audio-only stream that is put into a video session.
     */
    @JsonProperty("streamId")
    public String getStreamId() {
        return streamId;
    }

    /**
     * Creates an instance of this class from a JSON payload.
     *
     * @param json The JSON string to parse.
     * @return An instance of this class with the fields populated, if present.
     */
    public static OutboundSipResponse fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, OutboundSipResponse.class);
        }
        catch (IOException ex) {
            throw new VonageUnexpectedException("Failed to produce OutboundSipResponse from json.", ex);
        }
    }
}

