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
package com.vonage.client.account;

import com.vonage.client.VonageUnexpectedException;
import org.junit.Test;
import static org.junit.Assert.fail;

public class BalanceResponseTest {
    @Test
    public void testJsonError() throws Exception {
        try {
            BalanceResponse.fromJson("blarg");
            fail("Deserializing nonsense JSON should result in a VonageUnexpectedException");
        } catch (VonageUnexpectedException nue) {
            // This is expected
        }
    }
}
