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
package com.vonage.client.application;

import com.vonage.client.common.HalFilterRequest;

/**
 * Query parameters for {@link ApplicationClient#listApplications(ListApplicationRequest)}.
 */
public class ListApplicationRequest extends HalFilterRequest {

    private ListApplicationRequest(Builder builder) {
        super(builder);
    }

    @Override
    public Integer getPage() {
        return super.getPage();
    }

    @Override
    public Integer getPageSize() {
        return super.getPageSize();
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends HalFilterRequest.Builder<ListApplicationRequest, Builder> {

        @Deprecated
        public Builder() {
        }

        /**
         * @param pageSize The number of applications per page.
         *
         * @return This builder.
         */
        public Builder pageSize(long pageSize) {
            return super.pageSize((int) pageSize);
        }

        /**
         * @param page The current page number, starts at 1.
         *
         * @return This builder.
         */
        public Builder page(long page) {
            return super.page((int) page);
        }

        /**
         * @return A new {@link ListApplicationRequest} from the stored configuration.
         */
        public ListApplicationRequest build() {
            return new ListApplicationRequest(this);
        }
    }
}
