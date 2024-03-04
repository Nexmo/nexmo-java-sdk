/*
 *   Copyright 2024 Vonage
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
package com.vonage.client.conversations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.JsonableBaseObject;
import com.vonage.client.users.BaseUser;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class UserSession extends JsonableBaseObject {
	@JsonProperty("id") private String sessionId;
	@JsonProperty("_embedded") private Embedded _embedded;
	@JsonProperty("properties") private Properties properties;

	UserSession() {
	}

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Embedded extends JsonableBaseObject {
		@JsonProperty("api_key") String apiKey;
		@JsonProperty("user") BaseUser user;
	}

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Properties extends JsonableBaseObject {
		@JsonProperty("ttl") Double ttl;
	}

	/**
	 * Unique session identifier.
	 * 
	 * @return The session ID.
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * API key.
	 * 
	 * @return The API key, or {@code null} if unknown / not applicable.
	 */
	@JsonIgnore
	public String getApiKey() {
		return _embedded != null ? _embedded.apiKey : null;
	}

	/**
	 * Session time-to-live in minutes.
	 * 
	 * @return The session TTL in minutes, or {@code null} if unknown / unspecified.
	 */
	@JsonIgnore
	public Double getTtl() {
		return properties != null ? properties.ttl : null;
	}

	/**
	 * Basic user metadata.
	 * 
	 * @return The embedded user object, or {@code null} if absent.
	 */
	@JsonIgnore
	public BaseUser getUser() {
		return _embedded != null ? _embedded.user : null;
	}
}
