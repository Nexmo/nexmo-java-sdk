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
package com.vonage.client.meetings;

import com.vonage.client.HttpConfig;
import com.vonage.client.HttpWrapper;
import com.vonage.client.TestUtils;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class CreateThemeEndpointTest {
	private CreateThemeEndpoint endpoint;
	
	@Before
	public void setUp() {
		endpoint = new CreateThemeEndpoint(new HttpWrapper(new JWTAuthMethod("app-id", new byte[0])));
	}
	
	@Test
	public void testMakeRequestAllParameters() throws Exception {
		Theme request = Theme.builder()
				.mainColor("#AB34C1")
				.brandText("Vonage")
				.themeName("Test theme")
				.shortCompanyUrl("https://developer.vonage.com")
				.build();

		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals("POST", builder.getMethod());
		String expectedUri = "https://api-eu.vonage.com/meetings/themes";
		assertEquals(expectedUri, builder.build().getURI().toString());

		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		String expectedRequest = "{" +
				"\"theme_name\":\"Test theme\"," +
				"\"main_color\":\"#AB34C1\"," +
				"\"short_company_url\":\"https://developer.vonage.com\"," +
				"\"brand_text\":\"Vonage\"" +
				"}";
		assertEquals(expectedRequest, EntityUtils.toString(builder.getEntity()));

		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		String expectedResponse = "{\"theme_name\":\"fancy\",\"nonsense\":false}";
		HttpResponse mockResponse = TestUtils.makeJsonHttpResponse(200, expectedResponse);
		Theme parsed = endpoint.parseResponse(mockResponse);
		assertEquals("fancy", parsed.getThemeName());
		assertNull(parsed.getBrandText());
		assertNull(parsed.getThemeId());
		assertNull(parsed.getAccountId());
		assertNull(parsed.getApplicationId());
		assertNull(parsed.getBrandedFavicon());
		assertNull(parsed.getBrandImageColored());
		assertNull(parsed.getBrandImageWhite());
		assertNull(parsed.getBrandedFaviconUrl());
		assertNull(parsed.getBrandImageColoredUrl());
		assertNull(parsed.getBrandImageWhiteUrl());
	}

	@Test
	public void testCustomUri() throws Exception {
		String baseUri = "http://example.com";
		HttpWrapper wrapper = new HttpWrapper(HttpConfig.builder().baseUri(baseUri).build());
		endpoint = new CreateThemeEndpoint(wrapper);
		String expectedUri = baseUri + "/meetings/themes";
		Theme request = Theme.builder().build();
		RequestBuilder builder = endpoint.makeRequest(request);
		assertEquals(expectedUri, builder.build().getURI().toString());
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Content-Type").getValue());
		String expectedRequest = "{}";
		assertEquals(expectedRequest, EntityUtils.toString(builder.getEntity()));
		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), builder.getFirstHeader("Accept").getValue());
		assertEquals("POST", builder.getMethod());
	}

	@Test(expected = MeetingsResponseException.class)
	public void testUnsuccessfulResponse() throws Exception {
		endpoint.parseResponse(TestUtils.makeJsonHttpResponse(400, "{}"));
	}
}