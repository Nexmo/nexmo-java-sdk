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

import com.vonage.client.OrderedJsonMap;
import static com.vonage.client.OrderedJsonMap.entry;
import com.vonage.client.voice.TextToSpeechLanguage;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.net.URI;
import java.util.List;

public class AudioOutEventTest extends AbstractEventTest {
    private final String text = "Hello, hi testing";

    <E extends AudioOutEvent<?>, B extends AudioOutEvent.Builder<E, B>> E testAudioOutEventAllFields(
            EventType type, B builder, OrderedJsonMap bodyFields) {

        boolean queue = true;
        double level = 0.35;
        int loop = 6;

        var event = testBaseEvent(type,
                builder.loop(loop).level(level).queue(queue),
                new OrderedJsonMap(
                        entry("queue", queue),
                        entry("level", level),
                        entry("loop", loop)
                ).addAll(bodyFields)
        );
        assertEquals(loop, event.getLoop());
        assertEquals(level, event.getLevel());
        assertEquals(queue, event.getQueue());
        return event;
    }

    <E extends AudioOutEvent<?>, B extends AudioOutEvent.Builder<E, B>> E testAudioOutEventRequiredFields(
            EventType type, B builder, OrderedJsonMap bodyFields) {

        var event = testBaseEvent(type, builder, bodyFields);
        assertNull(event.getQueue());
        assertNull(event.getLevel());
        assertNull(event.getLoop());
        return event;
    }

    @Test
    public void testAudioPlayEventAllFields() {
        String streamUrl = "ftp://example.com/path/to/audio.mp3";
        var event = testAudioOutEventAllFields(EventType.AUDIO_PLAY,
                AudioPlayEvent.builder().streamUrl(streamUrl),
                new OrderedJsonMap(entry("stream_url", List.of(streamUrl)))
        );
        assertNull(event.getPlayId());
        assertEquals(URI.create(streamUrl), event.getStreamUrl());
    }

    @Test
    public void testAudioPlayEventRequiredFields() {
        var event = testAudioOutEventRequiredFields(EventType.AUDIO_PLAY,
                AudioPlayEvent.builder(), new OrderedJsonMap()
        );
        assertNull(event.getPlayId());
        assertNull(event.getStreamUrl());
    }

    @Test
    public void testAudioSayEventAllFields() {
        boolean ssml = false, premium = true;
        int style = 1;
        TextToSpeechLanguage language = TextToSpeechLanguage.DANISH;

        var event = testAudioOutEventAllFields(EventType.AUDIO_SAY,
                AudioSayEvent.builder().text(text).language(language)
                        .style(style).premium(premium).ssml(ssml),
                new OrderedJsonMap(
                        entry("text", text),
                        entry("style", style),
                        entry("language", language),
                        entry("premium", premium),
                        entry("ssml", ssml)
                )
        );
        assertNull(event.getSayId());
        assertEquals(text, event.getText());
        assertEquals(style, event.getStyle());
        assertEquals(language, event.getLanguage());
        assertEquals(premium, event.getPremium());
        assertEquals(ssml, event.getSsml());
    }

    @Test
    public void testAudioSayEventRequiredFields() {
        var event = testAudioOutEventRequiredFields(EventType.AUDIO_SAY,
                AudioSayEvent.builder().text(text),
                new OrderedJsonMap(entry("text", text))
        );
        assertEquals(text, event.getText());
        assertNull(event.getSayId());
        assertNull(event.getSsml());
        assertNull(event.getPremium());
        assertNull(event.getStyle());
        assertNull(event.getLanguage());
    }

    @Test
    public void testAudioSayEventNoText() {
        assertThrows(IllegalArgumentException.class, () -> applyBaseFields(AudioSayEvent.builder()).build());
    }
}
