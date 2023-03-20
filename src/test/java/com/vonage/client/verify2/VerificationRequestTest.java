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
package com.vonage.client.verify2;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Collections;
import java.util.Objects;

public class VerificationRequestTest {
	static final Locale LOCALE = Locale.PORTUGUESE_PORTUGAL;
	static final int CODE_LENGTH = 8, CHANNEL_TIMEOUT = 120;
	static final String
			BRAND = "Vonage",
			TO_NUMBER = "447700900000",
			TO_EMAIL = "alice@example.org",
			FROM_EMAIL = "bob@example.org",
			CLIENT_REF = "my-personal-reference",
			APP_HASH = "kkeid8sksd3";

	VerificationRequest.Builder newBuilder() {
		return VerificationRequest.builder().brand(BRAND);
	}

	VerificationRequest.Builder newBuilderAllParams() {
		return newBuilder().codeLength(CODE_LENGTH).clientRef(CLIENT_REF)
				.channelTimeout(CHANNEL_TIMEOUT).locale(LOCALE);
	}

	Workflow getWorkflowRequiredParamsForChannel(Channel channel) {
		switch (channel) {
			default: throw new IllegalStateException();
			case SMS:
				return new SmsWorkflow(TO_NUMBER);
			case VOICE:
				return new VoiceWorkflow(TO_NUMBER);
			case WHATSAPP:
				return new WhatsappWorkflow(TO_NUMBER);
			case WHATSAPP_INTERACTIVE:
				return new WhatsappCodelessWorkflow(TO_NUMBER);
			case EMAIL:
				return new EmailWorkflow(TO_EMAIL, FROM_EMAIL);
			case SILENT_AUTH:
				return new SilentAuthWorkflow(TO_NUMBER);
		}
	}

	Workflow getWorkflowAllParamsForChannel(Channel channel) {
		if (Objects.requireNonNull(channel) == Channel.SMS) {
			return new SmsWorkflow(TO_NUMBER, APP_HASH);
		}
		return getWorkflowRequiredParamsForChannel(channel);
	}

	VerificationRequest.Builder getBuilderRequiredParamsSingleWorkflow(Channel channel) {
		return newBuilder().addWorkflow(getWorkflowRequiredParamsForChannel(channel));
	}

	VerificationRequest.Builder getBuilderAllParamsSingleWorkflow(Channel channel) {
		return newBuilderAllParams().addWorkflow(getWorkflowAllParamsForChannel(channel));
	}

	String getExpectedRequiredParamsForSingleWorkflowJson(Channel channel) {
		String to = channel == Channel.EMAIL ? TO_EMAIL : TO_NUMBER;
		String expectedJson = "{\"brand\":\""+BRAND+"\",\"workflow\":[{" +
				"\"channel\":\""+channel+"\",\"to\":\""+to+"\"";
		if (channel == Channel.EMAIL) {
			expectedJson += ",\"from\":\""+FROM_EMAIL+"\"";
		}
		expectedJson += "}]}";
		return expectedJson;
	}

	String getExpectedAllParamsForSingleWorkflowJson(Channel channel) {
		String expectedJson = getExpectedRequiredParamsForSingleWorkflowJson(channel), prefix, replacement;
		if (channel == Channel.SMS) {
			prefix = TO_NUMBER + "\"";
			replacement = prefix + ",\"app_hash\":\""+APP_HASH+"\"";
			expectedJson = expectedJson.replace(prefix, replacement);
		}
		expectedJson = "{\"locale\":\"pt-pt\",\"channel_timeout\":"+ CHANNEL_TIMEOUT +
				",\"code_length\":"+CODE_LENGTH + expectedJson.replaceFirst("\\{", ",");
		prefix = BRAND + "\",";
		replacement = prefix + "\"client_ref\":\""+CLIENT_REF+"\",";
		expectedJson = expectedJson.replace(prefix, replacement);
		return expectedJson;
	}

	void assertEqualsRequiredParams(Channel channel) {
		String expectedJson = getExpectedRequiredParamsForSingleWorkflowJson(channel);
		String actualJson = getBuilderRequiredParamsSingleWorkflow(channel).build().toJson();
		assertEquals(expectedJson, actualJson);
	}

	void assertEqualsAllParams(Channel channel) {
		String expectedJson = getExpectedAllParamsForSingleWorkflowJson(channel);
		String actualJson = getBuilderAllParamsSingleWorkflow(channel).build().toJson();
		assertEquals(expectedJson, actualJson);
	}

	@Test
	public void testAllChannelsValidParams() {
		for (Channel channel : Channel.values()) {
			assertEqualsRequiredParams(channel);
			assertEqualsAllParams(channel);
		}
	}

	@Test
	public void testWithoutWorkflow() {
		assertThrows(IllegalStateException.class, () -> newBuilder().build());
		assertThrows(IllegalStateException.class, () -> newBuilderAllParams().build());
		assertThrows(IllegalStateException.class, () -> newBuilder().workflows(Collections.emptyList()).build());
		assertThrows(IllegalStateException.class, () -> newBuilderAllParams().workflows(Collections.emptyList()).build());
	}

	@Test
	public void testWithoutBrand() {
		for (Channel channel : Channel.values()) {
			VerificationRequest.Builder builder = getBuilderRequiredParamsSingleWorkflow(channel);
			assertEquals(BRAND, builder.build().getBrand());
			builder.brand(null);
			assertNull(builder.brand);
			assertThrows(IllegalArgumentException.class, builder::build);
			builder.brand("");
			assertTrue(builder.brand.isEmpty());
			assertThrows(IllegalArgumentException.class, builder::build);
		}
	}

	@Test
	public void testAllWorkflowsWithoutRecipientOrSender() {
		for (String invalid : new String[]{"", " ", null}) {
			assertThrows(IllegalArgumentException.class, () -> new SilentAuthWorkflow(invalid));
			assertThrows(IllegalArgumentException.class, () -> new SmsWorkflow(invalid));
			assertThrows(IllegalArgumentException.class, () -> new VoiceWorkflow(invalid));
			assertThrows(IllegalArgumentException.class, () -> new WhatsappWorkflow(invalid));
			assertThrows(IllegalArgumentException.class, () -> new WhatsappCodelessWorkflow(invalid));
			assertThrows(IllegalArgumentException.class, () -> new EmailWorkflow(invalid, "hello@example.org"));
			assertThrows(IllegalArgumentException.class, () -> new EmailWorkflow("hello@example.org", invalid));
		}
	}

	@Test
	public void testCodeLengthBoundaries() {
		VerificationRequest.Builder builder = getBuilderAllParamsSingleWorkflow(Channel.VOICE);
		assertEquals(CODE_LENGTH, builder.build().getCodeLength().intValue());
		int min = 4, max = 10;
		builder.codeLength(min - 1);
		assertThrows(IllegalArgumentException.class, builder::build);
		builder.codeLength(min);
		assertEquals(min, builder.build().getCodeLength().intValue());
		builder.codeLength(max + 1);
		assertThrows(IllegalArgumentException.class, builder::build);
		builder.codeLength(max);
		assertEquals(max, builder.build().getCodeLength().intValue());
	}

	@Test
	public void testChannelTimeoutBoundaries() {
		VerificationRequest.Builder builder = getBuilderAllParamsSingleWorkflow(Channel.VOICE);
		assertEquals(CHANNEL_TIMEOUT, builder.build().getChannelTimeout().intValue());
		int min = 60, max = 900;
		builder.channelTimeout(min - 1);
		assertThrows(IllegalArgumentException.class, builder::build);
		builder.channelTimeout(min);
		assertEquals(min, builder.build().getChannelTimeout().intValue());
		builder.channelTimeout(max + 1);
		assertThrows(IllegalArgumentException.class, builder::build);
		builder.channelTimeout(max);
		assertEquals(max, builder.build().getChannelTimeout().intValue());
	}

	@Test
	public void testInvalidAppHash() {
		assertThrows(IllegalArgumentException.class, () -> new SmsWorkflow(TO_NUMBER, "1234567890"));
		assertThrows(IllegalArgumentException.class, () -> new SmsWorkflow(TO_NUMBER, "1234567890ab"));
		String appHash = new SmsWorkflow(TO_NUMBER, "1234567890a").getAppHash();
		assertNotNull(appHash);
		assertEquals(11, appHash.length());
	}
}
