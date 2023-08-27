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
package com.vonage.client.numbers;

import com.fasterxml.jackson.annotation.*;
import com.vonage.client.VonageApiResponseException;

/**
 * Response returned when an error is encountered (i.e. the API returns a non-2xx status code).
 *
 * @since 7.8.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class NumbersResponseException extends VonageApiResponseException {
	@JsonProperty("error-code") String errorCode;
	@JsonProperty("error-code-label") String errorCodeLabel;

	@JsonSetter("error-code")
	private void setErrorCode(String errorCode) {
		if (errorCode != null && !errorCode.trim().isEmpty()) {
			setStatusCode(Integer.parseInt(errorCode));
		}
	}

	/**
	 * The status code description.
	 *
	 * @return The error code label, or {@code null} if unknown.
	 */
	public String getErrorCodeLabel() {
		return errorCodeLabel;
	}

	void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * Creates an instance of this class from a JSON payload.
	 *
	 * @param json The JSON string to parse.
	 * @return An instance of this class with all known fields populated from the JSON payload, if present.
	 */
	@JsonCreator
	public static NumbersResponseException fromJson(String json) {
		return fromJson(NumbersResponseException.class, json);
	}
}
