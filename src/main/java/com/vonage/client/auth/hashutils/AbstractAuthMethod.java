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
package com.vonage.client.auth.hashutils;

import com.vonage.client.auth.AuthMethod;
import java.util.Objects;

/**
 * Base class for all AuthMethod implementations.
 *
 * @since 8.8.0
 */
public abstract class AbstractAuthMethod implements AuthMethod {

    @Override
    public final int compareTo(AuthMethod other) {
        return AuthMethod.super.compareTo(other);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AuthMethod)) {
            return false;
        }
        return ((AuthMethod) obj).getSortKey() == this.getSortKey() &&
                obj.getClass().equals(this.getClass());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSortKey(), getClass());
    }
}
