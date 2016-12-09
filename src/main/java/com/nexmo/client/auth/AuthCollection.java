package com.nexmo.client.auth;/*
 * Copyright (c) 2011-2016 Nexmo Inc
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

import com.nexmo.client.NexmoClientException;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class AuthCollection {
    private SortedSet<AuthMethod> authList;

    public AuthCollection() {
        this.authList = new TreeSet<AuthMethod>();
    }

    public void add(AuthMethod auth) {
        this.authList.add(auth);
    }

    public AuthMethod getAcceptableAuthMethod(Set<Class> acceptableAuthMethods) throws NexmoClientException {
        for (AuthMethod availableType : this.authList) {
            if (acceptableAuthMethods.contains(availableType.getClass())) {
                return availableType;
            }
        }
        throw new NexmoUnacceptableAuthException(this.authList, acceptableAuthMethods);
    }
}
