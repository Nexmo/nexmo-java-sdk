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
package com.vonage.client.subaccounts;

import com.vonage.client.AbstractMethod;
import com.vonage.client.HttpWrapper;
import com.vonage.client.auth.TokenAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import java.io.IOException;
import java.util.List;

class ListBalanceTransfersEndpoint extends AbstractMethod<ListTransfersFilter, List<BalanceTransfer>> {
	private static final Class<?>[] ALLOWED_AUTH_METHODS = {TokenAuthMethod.class};
	private static final String PATH = "/accounts/%s/balance-transfers";

	ListBalanceTransfersEndpoint(HttpWrapper httpWrapper) {
		super(httpWrapper);
	}

	@Override
	protected Class<?>[] getAcceptableAuthMethods() {
		return ALLOWED_AUTH_METHODS;
	}

	@Override
	public RequestBuilder makeRequest(ListTransfersFilter request) {
		String path = String.format(PATH, getApplicationIdOrApiKey());
		String uri = httpWrapper.getHttpConfig().getApiBaseUri() + path;
		return request.addParams(RequestBuilder.get(uri)
				.setHeader("Accept", "application/json"));
	}

	@Override
	public List<BalanceTransfer> parseResponse(HttpResponse response) throws IOException {
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode >= 200 && statusCode < 300) {
			String json = basicResponseHandler.handleResponse(response);
			return ListTransfersResponseWrapper.fromJson(json).getBalanceTransfers();
		}
		else {
			throw SubaccountsResponseException.fromHttpResponse(response);
		}
	}
}
