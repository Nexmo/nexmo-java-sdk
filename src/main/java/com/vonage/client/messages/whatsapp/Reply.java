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
package com.vonage.client.messages.whatsapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Used for inbound interactive messages.
 *
 * @since 7.2.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Reply {
	private String id, title, description;

	Reply() {}

	/**
	 * An identifier to help identify the exact interactive message response.
	 *
	 * @return The reply ID.
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	/**
	 * The title displayed on the interactive option chosen.
	 *
	 * @return The chosen option's title.
	 */
	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	/**
	 * A description that may be added to the interactive options presented (available only on interactive lists).
	 *
	 * @return The description, or {@code null} if not applicable.
	 */
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}
}
