/*
 * Copyright (c) 2011-2017 Nexmo Inc
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
package com.nexmo.client.auth;


import com.nexmo.client.TestUtils;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AuthCollectionTest {
    private TestUtils testUtils = new TestUtils();

    @Test
    public void testGetAcceptableAuthMethod() throws Exception {
        JWTAuthMethod jAuth = new JWTAuthMethod("application_id", testUtils.loadKey("test/keys/application_key"));
        AuthCollection auths = new AuthCollection();
        auths.add(jAuth);

        Set<Class> acceptableAuths = acceptableClassSet(JWTAuthMethod.class);

        assertEquals(jAuth, auths.getAcceptableAuthMethod(acceptableAuths));
    }

    @Test
    public void testMultipleAuthMethods() throws Exception {
        JWTAuthMethod jwtAuth = new JWTAuthMethod("application_id", testUtils.loadKey("test/keys/application_key"));
        TokenAuthMethod tokenAuth = new TokenAuthMethod("api_key", "api_secret");

        AuthCollection auths = new AuthCollection(
                jwtAuth, tokenAuth
        );

        assertEquals(jwtAuth, auths.getAcceptableAuthMethod(acceptableClassSet(JWTAuthMethod.class)));
        assertEquals(tokenAuth, auths.getAcceptableAuthMethod(acceptableClassSet(TokenAuthMethod.class)));
    }

    @Test
    public void testNoAcceptableAuthMethod() throws Exception {
        AuthCollection auths = new AuthCollection();

        Set<Class> acceptableAuths = new HashSet<>();
        acceptableAuths.add(JWTAuthMethod.class);

        try {
            auths.getAcceptableAuthMethod(acceptableAuths);
            fail("No acceptable auth method should throw a NexmoClientException");
        } catch (NexmoUnacceptableAuthException ex) {
            assertEquals(
                    "No acceptable authentication type could be found. Acceptable types are: JWTAuthMethod. Supplied " +
                            "types were: ",
                    ex.getMessage());
        }
    }

    @Test
    public void testAuthMethodPrecedence() throws Exception {
        JWTAuthMethod jAuth = new JWTAuthMethod("application_id", testUtils.loadKey("test/keys/application_key"));
        TokenAuthMethod tAuth = new TokenAuthMethod("key", "secret");
        AuthCollection auths = new AuthCollection();
        auths.add(tAuth);
        auths.add(jAuth);

        Set<Class> acceptableAuths = new HashSet<>();
        acceptableAuths.add(JWTAuthMethod.class);

        assertEquals(jAuth, auths.getAcceptableAuthMethod(acceptableAuths));
    }

    @Test
    public void testIncompatibleAuths() throws Exception {
        TokenAuthMethod tAuth = new TokenAuthMethod("key", "secret");
        AuthCollection auths = new AuthCollection();
        auths.add(tAuth);

        Set<Class> acceptableAuths = new HashSet<>();
        acceptableAuths.add(JWTAuthMethod.class);

        try {
            auths.getAcceptableAuthMethod(acceptableAuths);
            fail("No acceptable auth method should throw a NexmoClientException");
        } catch (NexmoUnacceptableAuthException ex) {
            assertEquals("No acceptable authentication type could be found. Acceptable types are: JWTAuthMethod. Supplied " +
                    "types were: TokenAuthMethod", ex.getMessage());
        }
    }

    public Set<Class> acceptableClassSet(Class... classes) {
        Set<Class> result = new HashSet<>();
        Collections.addAll(result, classes);
        return result;
    }
}
