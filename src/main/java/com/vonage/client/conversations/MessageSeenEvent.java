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
package com.vonage.client.conversations;

/**
 * Represents a {@link EventType#MESSAGE_SEEN} event.
 */
public final class MessageSeenEvent extends MessageStatusEvent {

    MessageSeenEvent() {}

    MessageSeenEvent(Builder builder) {
        super(builder);
    }

    /**
     * Entry point for constructing an instance of this class.
     *
     * @return A new Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for configuring parameters of the event request.
     */
    public static class Builder extends MessageStatusEvent.Builder<MessageSeenEvent, Builder> {

        Builder() {
            super(EventType.MESSAGE_SEEN);
        }

        @Override
        public MessageStatusEvent build() {
            return new MessageSeenEvent(this);
        }
    }
}
