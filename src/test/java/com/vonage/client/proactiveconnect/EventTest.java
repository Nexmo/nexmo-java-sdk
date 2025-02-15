/*
 *   Copyright 2025 Vonage
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
package com.vonage.client.proactiveconnect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vonage.client.TestUtils;
import com.vonage.client.VonageResponseParseException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class EventTest {
	
	@Test
	public void testFromJsonAllFields() throws Exception {
		UUID id = UUID.randomUUID(), jobId = UUID.randomUUID(),
				runId = UUID.randomUUID(), runItemId = UUID.randomUUID(),
				actionId = UUID.randomUUID(), invocationId = UUID.randomUUID();

		Map<String, Object> data = new LinkedHashMap<>();
		data.put("foo", 1.778);
		data.put("baz", 3);
		data.put("test_bool", true);
		data.put("qux", "A String");
		String recipientId = "15551584817";
		String sourceContext = "UK recipient";
		Instant occurredAt = Instant.parse("2022-03-20T20:41:59.086Z");
		EventType type = EventType.RUN_ITEMS_EXCLUDED;
	
		Event response = Event.fromJson("{\n" +
				"\"id\":\""+id+"\",\n" +
				"\"job_id\":\""+jobId+"\",\n" +
				"\"run_id\":\""+runId+"\",\n" +
				"\"run_item_id\":\""+runItemId+"\",\n" +
				"\"action_id\":\""+actionId+"\",\n" +
				"\"invocation_id\":\""+invocationId+"\",\n" +
				"\"data\":"+new ObjectMapper().writeValueAsString(data)+",\n" +
				"\"recipient_id\":\""+recipientId+"\",\n" +
				"\"src_ctx\":\""+sourceContext+"\",\n" +
				"\"occurred_at\":\""+occurredAt+"\",\n" +
				"\"type\":\""+type+"\"\n" +
		"}");

		TestUtils.testJsonableBaseObject(response);
		assertEquals(id, response.getId());
		assertEquals(jobId, response.getJobId());
		assertEquals(runId, response.getRunId());
		assertEquals(runItemId, response.getRunItemId());
		assertEquals(actionId, response.getActionId());
		assertEquals(invocationId, response.getInvocationId());
		assertEquals(data, response.getData());
		assertEquals(recipientId, response.getRecipientId());
		assertEquals(sourceContext, response.getSourceContext());
		assertEquals(occurredAt, response.getOccurredAt());
		assertEquals(type, response.getType());
	}
	
	@Test
	public void testFromJsonInvalid() {
		assertThrows(VonageResponseParseException.class, () -> Event.fromJson("{malformed]"));
	}

	@Test
	public void testFromJsonEmpty() {
		Event response = Event.fromJson("{}");
		TestUtils.testJsonableBaseObject(response);
		assertNull(response.getId());
		assertNull(response.getJobId());
		assertNull(response.getRunId());
		assertNull(response.getRunItemId());
		assertNull(response.getActionId());
		assertNull(response.getInvocationId());
		assertNull(response.getData());
		assertNull(response.getRecipientId());
		assertNull(response.getSourceContext());
		assertNull(response.getOccurredAt());
		assertNull(response.getType());
	}

	@Test
	public void testInvalidEventType() {
		Event valid = Event.fromJson("{\"type\":\"run_item_skipped\"}");
		assertEquals(EventType.RUN_ITEM_SKIPPED, valid.getType());
		Event invalid = Event.fromJson("{\"type\":\"blahh\"}");
		assertNull(invalid.getType());
	}
}