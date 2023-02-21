/* Copyright 2022 Vonage
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.vonage.client.video;

import com.vonage.client.VonageUnexpectedException;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.UUID;

public class CreateSessionResponseTest {

	@Test
	public void testFromJsonAllFields() {
		String sessionId = "SESSION_ID-123",
			applicationId = UUID.randomUUID().toString(),
			createDt = "abc123",
			mediaServerUrl = "ftp://myserver.data/resource";

		CreateSessionResponse response = CreateSessionResponse.fromJson("[{\n" +
				"\"session_id\":\""+sessionId+"\",\n" +
				"\"application_id\":\""+applicationId+"\",\n" +
				"\"create_dt\":\""+createDt+"\",\n" +
				"\"media_server_url\":\""+mediaServerUrl+"\"\n" +
		"}]");

		assertEquals(sessionId, response.getSessionId());
		assertEquals(applicationId, response.getApplicationId());
		assertEquals(createDt, response.getCreateDt());
		assertEquals(mediaServerUrl, response.getMediaServerUrl());
	}

	@Test(expected = VonageUnexpectedException.class)
	public void testFromJsonInvalid() {
		CreateSessionResponse.fromJson("{malformed]");
	}

	@Test
	public void testFromJsonEmptyObject() {
		CreateSessionResponse response = CreateSessionResponse.fromJson("[{}]");
		assertNull(response.getApplicationId());
		assertNull(response.getSessionId());
		assertNull(response.getMediaServerUrl());
		assertNull(response.getCreateDt());
	}

	@Test
	public void testFromJsonEmptyArray() {
		CreateSessionResponse response = CreateSessionResponse.fromJson("[]");
		assertNotNull(response);
	}

	@Test
	public void testFromJsonMultipleEntries() {
		String sessionId = "TheSessionIdYouWant";
		String json = "[{\"session_id\":\""+sessionId+"\"},{},{\"session_id\":\"fake\"}]";
		CreateSessionResponse response = CreateSessionResponse.fromJson(json);
		assertNotNull(response);
		assertEquals(sessionId, response.getSessionId());
		assertNull(response.getApplicationId());
		assertNull(response.getMediaServerUrl());
		assertNull(response.getCreateDt());
	}
}
