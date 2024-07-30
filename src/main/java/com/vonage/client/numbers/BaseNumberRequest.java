/*
 *   Copyright 2024 Vonage
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
package com.vonage.client.numbers;

import com.vonage.client.QueryParamsRequest;
import com.vonage.client.common.E164;
import java.util.LinkedHashMap;
import java.util.Map;

abstract class BaseNumberRequest implements QueryParamsRequest {
    private final String country, msisdn;

    protected BaseNumberRequest(String country, String msisdn) {
        if ((this.country = country) == null || country.length() != 2) {
            throw new IllegalArgumentException("Country code is required in ISO 3166-1 alpha-2 format.");
        }
        this.msisdn = new E164(msisdn).toString();
    }

    public String getCountry() {
        return country;
    }

    public String getMsisdn() {
        return msisdn;
    }

    @Override
    public Map<String, String> makeParams() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>(4);
        params.put("country", country);
        params.put("msisdn", msisdn);
        return params;
    }
}
