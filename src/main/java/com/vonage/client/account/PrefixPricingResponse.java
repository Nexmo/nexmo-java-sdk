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
package com.vonage.client.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vonage.client.Jsonable;
import com.vonage.client.JsonableBaseObject;
import java.util.List;

/**
 * Wrapper for the response in {@link AccountClient#getPrefixPrice(ServiceType, String)}.
 *
 * @deprecated This will be removed in favour of returning the list of countries' pricing directly.
 */
@Deprecated
public class PrefixPricingResponse extends JsonableBaseObject {
    private int count;
    private List<PricingResponse> countries;

    @JsonProperty("count")
    public int getCount() {
        return count;
    }

    @JsonProperty("countries")
    public List<PricingResponse> getCountries() {
        return countries;
    }

    public static PrefixPricingResponse fromJson(String json) {
        return Jsonable.fromJson(json);
    }
}
