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

import com.nexmo.jwt.Jwt;
import org.apache.http.client.methods.RequestBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JWTAuthMethod extends AbstractAuthMethod {
    private static final int SORT_KEY = 10;
    private Jwt jwt;

    public JWTAuthMethod(final String applicationId, final byte[] privateKey) {
        this.jwt = Jwt.builder().applicationId(applicationId).privateKeyContents(new String(privateKey)).build();
    }

    public JWTAuthMethod(String applicationId, Path path) throws IOException {
        this(applicationId, Files.readAllBytes(path));
    }

    public String generateToken() {
        return this.jwt.generate();
    }

    @Override
    public RequestBuilder apply(RequestBuilder request) {
        String token = this.jwt.generate();

        request.setHeader("Authorization", "Bearer " + token);
        return request;
    }

    @Override
    public int getSortKey() {
        return SORT_KEY;
    }
}
