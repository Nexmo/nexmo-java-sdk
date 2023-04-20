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
package com.vonage.client.proactiveconnect;

import com.vonage.client.AbstractMethod;
import com.vonage.client.HttpWrapper;
import com.vonage.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ByteArrayEntity;
import java.io.IOException;

class UploadListItemsEndpoint extends AbstractMethod<UploadListItemsRequestWrapper, UploadListItemsResponse> {
	private static final Class<?>[] ALLOWED_AUTH_METHODS = {JWTAuthMethod.class};
	private static final String PATH = "/v0.1/bulk/lists/%s/items/import";

	UploadListItemsEndpoint(HttpWrapper httpWrapper) {
		super(httpWrapper);
	}

	@Override
	protected Class<?>[] getAcceptableAuthMethods() {
		return ALLOWED_AUTH_METHODS;
	}

	@Override
	public RequestBuilder makeRequest(UploadListItemsRequestWrapper wrapper) {
		String path = String.format(PATH, wrapper.listId);
		String uri = httpWrapper.getHttpConfig().getApiEuBaseUri() + path;
		return RequestBuilder.post(uri)
				.setHeader("Content-Type", "multipart/form-data")
				.setHeader("Accept", "application/json")
				.setEntity(new ByteArrayEntity(wrapper.data));
	}

	@Override
	public UploadListItemsResponse parseResponse(HttpResponse response) throws IOException {
		return UploadListItemsResponse.fromJson(basicResponseHandler.handleResponse(response));
	}
}
