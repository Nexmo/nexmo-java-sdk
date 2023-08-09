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
package com.vonage.client.users.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Represents a Vonage Business Cloud (VBC) channel.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Vbc extends Channel {
	private String extension;

	protected Vbc() {}

	/**
	 * Creates a new VBC channel.
	 *
	 * @param extension The VBC extension number.
	 */
	public Vbc(int extension) {
		this.extension = String.valueOf(extension);
	}

	/**
	 * VBC extension.
	 * 
	 * @return The extension as a string.
	 */
	@JsonProperty("extension")
	public String getExtension() {
		return extension;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Vbc vbc = (Vbc) o;
		return Objects.equals(extension, vbc.extension);
	}

	@Override
	public int hashCode() {
		return Objects.hash(extension);
	}
}
