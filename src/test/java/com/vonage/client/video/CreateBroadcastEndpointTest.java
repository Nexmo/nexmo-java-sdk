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
package com.vonage.client.video;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;

public class CreateBroadcastEndpointTest {
	private CreateBroadcastEndpoint endpoint;
	private final String applicationId = UUID.randomUUID().toString();

	@Before
	public void setUp() {
		endpoint = new CreateBroadcastEndpoint(new HttpWrapper(
			new JWTAuthMethod(applicationId, new byte[0])
		));
	}
	
	@Test
	public void testMakeRequestRequiredParameters() throws Exception {
		Broadcast request = Broadcast.builder("SESSION_id123")
				.addRtmpStream(Rtmp.builder()
						.streamName("My Test Stream")
						.serverUrl("rtmps://mytestserver/mytestapp")
						.build()
				).build();
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("POST", builder.getMethod());
		String expectedUri = "https://video.api.vonage.com/v2/project/"+applicationId+"/broadcast";
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		String expectedRequest = "{\"sessionId\":\"SESSION_id123\",\"outputs\":{\"rtmp\":[{" +
				"\"streamName\":\"My Test Stream\",\"serverUrl\":\"rtmps://mytestserver/mytestapp\"}]}}";
		assertEquals(expectedRequest, EntityUtils.toString(builder.getEntity()));
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		String expectedResponse = "{}";
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, expectedResponse);
		Broadcast parsed = endpoint.parseResponse(mockResponse);
		assertNotNull(parsed);
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(
				HttpConfig.builder().videoBaseUri(baseUri).build(),
				new JWTAuthMethod(applicationId, new byte[0])
		);
		endpoint = new CreateBroadcastEndpoint(wrapper);
		String expectedUri = baseUri + "/v2/project/"+applicationId+"/broadcast";
		Broadcast request = Broadcast.builder("S").hls(Hls.builder().build()).build();
		RequestBuilder builder = endpoint.makeRequest(request);
		String expectedRequest = "{\"sessionId\":\"S\",\"outputs\":{\"hls\":{}}}";
		assertEquals(expectedRequest, EntityUtils.toString(builder.getEntity()));
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("POST", builder.getMethod());
	}
	
	@Test(expected = HttpResponseException.class)
	public void test500Response() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(500, ""));
	}

	@Test
	public void testParseFromFresh() throws Exception {
		HttpResponse mock = TestUtils.makeJsonHttpResponse(200, VideoClientTest.broadcastJson);
		HttpWrapper wrapper = new HttpWrapper(new JWTAuthMethod(applicationId, new byte[0]));
		endpoint = new CreateBroadcastEndpoint(wrapper);
		Broadcast parsed = endpoint.parseResponse(mock);
		VideoClientTest.assertBroadcastEqualsExpectedJson(parsed);
	}
}