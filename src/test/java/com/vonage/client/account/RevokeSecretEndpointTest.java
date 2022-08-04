/*
 *   Copyright 2022 Vonage
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

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RevokeSecretEndpointTest {
    private RevokeSecretEndpoint method;

    @Before
    public void setUp() {
        method = new RevokeSecretEndpoint(new HttpWrapper());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructParamsWithMissingApiKey() throws Exception {
        RevokeSecretEndpoint method = new RevokeSecretEndpoint(null);
        SecretRequest request = new SecretRequest(null, "secret-id");

        method.makeRequest(request);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructParamsWithMissingSecretId() throws Exception {
        RevokeSecretEndpoint method = new RevokeSecretEndpoint(null);
        SecretRequest request = new SecretRequest("account-id", null);

        method.makeRequest(request);
    }

    @Test
    public void testDefaultUri() throws Exception {
        SecretRequest request = new SecretRequest("account-id", "secret-id");

        RequestBuilder builder = method.makeRequest(request);
        assertEquals("DELETE", builder.getMethod());
        assertEquals("https://api.nexmo.com/accounts/account-id/secrets/secret-id",
                builder.build().getURI().toString()
        );
    }

    @Test
    public void testCustomUri() throws Exception {
        HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri("https://example.com").build());
        RevokeSecretEndpoint method = new RevokeSecretEndpoint(wrapper);
        SecretRequest request = new SecretRequest("account-id", "secret-id");

        RequestBuilder builder = method.makeRequest(request);
        assertEquals("DELETE", builder.getMethod());
        assertEquals("https://example.com/accounts/account-id/secrets/secret-id", builder.build().getURI().toString());
    }
}
