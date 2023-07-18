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
package com.vonage.client.application;

import com.vonage.client.application.capabilities.Capability;
import com.vonage.client.application.capabilities.Messages;
import com.vonage.client.application.capabilities.Rtc;
import com.vonage.client.application.capabilities.Voice;
import com.vonage.client.common.HttpMethod;
import com.vonage.client.common.Webhook;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.UUID;

public class ApplicationTest {
    @Test
    public void testEmpty() {
        String json = "{}";
        Application application = Application.builder().build();

        assertEquals(json, application.toJson());
    }

    @Test
    public void testName() {
        String json = "{\"name\":\"name\"}";
        Application application = Application.builder().name("name").build();

        assertEquals(json, application.toJson());
    }

    @Test
    public void testPublicKey() {
        String json = "{\"keys\":{\"public_key\":\"public key\"}}";
        Application application = Application.builder().publicKey("public key").build();

        assertEquals(json, application.toJson());
    }

    @Test
    public void testAddCapability() {
        String json = "{\"capabilities\":{\"voice\":{}}}";
        Application application = Application.builder().addCapability(Voice.builder().build()).build();

        assertEquals(json, application.toJson());
    }

    @Test
    public void testAddMultipleCapabilitiesOfSameType() {
        String expectedJson = "{\"capabilities\":{\"voice\":{\"webhooks\":{" +
                "\"fallback_answer_url\":{\"connectionTimeout\":500,\"socketTimeout\":3600," +
                "\"address\":\"https://fallback.example.com/webhooks/answer\",\"http_method\":\"GET\"}}}}}";

        Application application = Application.builder()
                .addCapability(Voice.builder().addWebhook(Webhook.Type.EVENT, Webhook.builder()
                        .address("https://example.com/webhooks/event").method(HttpMethod.POST).build()
                ).build())
                .addCapability(Voice.builder().addWebhook(Webhook.Type.FALLBACK_ANSWER,
                        Webhook.builder().method(HttpMethod.GET)
                                .address("https://fallback.example.com/webhooks/answer")
                                .connectionTimeout(500).socketTimeout(3600).build()
                ).build())
                .build();

        assertEquals(expectedJson, application.toJson());
    }

    @Test
    public void testAddMultipleCapabilitiesOfDifferentType() {
        String json = "{\"capabilities\":{\"voice\":{},\"rtc\":{}}}";
        Application application = Application.builder()
                .addCapability(Voice.builder().build())
                .addCapability(Rtc.builder().build())
                .build();

        assertEquals(json, application.toJson());
    }

    @Test
    public void testRemoveCapability() {
        String json = "{\"capabilities\":{\"rtc\":{}}}";
        Application application = Application.builder()
                .addCapability(Voice.builder().build())
                .addCapability(Rtc.builder().build())
                .removeCapability(Capability.Type.VOICE)
                .build();

        assertEquals(json, application.toJson());
    }

    @Test
    public void testRemoveMultipleCapabilities() {
        String json = "{}";
        Application application = Application.builder()
                .addCapability(Voice.builder().build())
                .addCapability(Rtc.builder().build())
                .removeCapability(Capability.Type.VOICE)
                .removeCapability(Capability.Type.RTC)
                .build();

        assertEquals(json, application.toJson());
    }

    @Test
    public void testRemoveCapabilityThatDoesntExist() {
        String json = "{\"capabilities\":{\"voice\":{},\"rtc\":{}}}";
        Application application = Application.builder()
                .addCapability(Voice.builder().build())
                .addCapability(Rtc.builder().build())
                .removeCapability(Capability.Type.VBC)
                .build();

        assertEquals(json, application.toJson());
    }

    @Test
    public void testAddMessagesCapabilityWithInboundAndStatusWebhooks() {
        String json = "{\"capabilities\":{\"messages\":{\"webhooks\":{\"inbound_url\":{\"address\":\"https://example.com/inbound\",\"http_method\":\"POST\"},\"status_url\":{\"address\":\"https://example.com/status\",\"http_method\":\"GET\"}}}}}";
        Application application = Application.builder()
                .addCapability(
                        Messages.builder()
                                .addWebhook(Webhook.Type.INBOUND, new Webhook("https://example.com/inbound", HttpMethod.POST))
                                .addWebhook(Webhook.Type.STATUS, new Webhook("https://example.com/status", HttpMethod.GET))
                                .build())
                .build();

        assertEquals(json, application.toJson());
    }

    @Test
    public void testAddMessagesCapabilityWithInboundWebhook() {
        String json = "{\"capabilities\":{\"messages\":{\"webhooks\":{\"inbound_url\":{\"address\":\"https://example.com/inbound\",\"http_method\":\"POST\"}}}}}";
        Application application = Application.builder()
                .addCapability(
                        Messages.builder()
                                .addWebhook(Webhook.Type.INBOUND, new Webhook("https://example.com/inbound", HttpMethod.POST))
                                .build())
                .build();

        assertEquals(json, application.toJson());
    }

    @Test
    public void testUpdatingApplication() {
        String json = "{\"name\":\"updated\"}";
        Application initial = Application.builder().name("initial").build();
        Application updated = Application.builder(initial).name("updated").build();

        assertEquals(json, updated.toJson());
    }

    @Test
    public void testPrivacy() {
        String json = "{\"privacy\":{";

        Application request = Application.builder().improveAi(true).build();
        assertEquals(json + "\"improve_ai\":"+request.getPrivacy().getImproveAi()+"}}", request.toJson());
        Application parsed = Application.fromJson(json + "}}");
        assertNotNull(parsed.getPrivacy());
        assertNull(parsed.getPrivacy().getImproveAi());

        parsed.updateFromJson(request.toJson());
        assertTrue(parsed.getPrivacy().getImproveAi());

        assertNull(request.getId());
        request.updateFromJson(json + "}, \"id\": \""+ UUID.randomUUID()+"\"}");
        assertNotNull(request.getId());
        assertNotNull(request.getPrivacy());
        assertNull(request.getPrivacy().getImproveAi());

    }
}
