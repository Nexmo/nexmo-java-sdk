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
package com.vonage.client.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;

class CreateSecretRequest implements Jsonable {
    @JsonIgnore private final String apiKey;
    private final String secret;

    public CreateSecretRequest(String apiKey, String secret) {
        this.apiKey = apiKey;
        this.secret = secret;
    }

    public String getApiKey() {
        return apiKey;
    }

    @JsonProperty("secret")
    public String getSecret() {
        return secret;
    }
}
