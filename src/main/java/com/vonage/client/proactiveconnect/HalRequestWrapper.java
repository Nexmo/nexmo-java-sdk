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

import org.apache.http.client.methods.RequestBuilder;

class HalRequestWrapper {
	final Integer page, pageSize;
	final SortOrder order;
	final String id;

	HalRequestWrapper(Integer page, Integer pageSize, SortOrder order, String id) {
		this.page = page;
		this.pageSize = pageSize;
		this.order = order;
		this.id = id;
	}

	RequestBuilder addParams(RequestBuilder builder) {
		if (page != null) {
			builder.addParameter("page", page.toString());
		}
		if (pageSize != null) {
			builder.addParameter("page_size", pageSize.toString());
		}
		if (order != null) {
			builder.addParameter("order", order.toString());
		}
		return builder;
	}
}
