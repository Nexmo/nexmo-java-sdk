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
package com.nexmo.client.voice;

import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.voice.ncco.InputAction;
import com.nexmo.client.voice.ncco.Ncco;
import com.nexmo.client.voice.ncco.RecordAction;
import com.nexmo.client.voice.ncco.TalkAction;
import org.junit.Test;

import static org.junit.Assert.*;

public class CallTest {
    @Test
    public void testToJson() throws Exception {
        Call call = new Call("4477999000", "44111222333", "https://callback.example.com/");
        assertEquals(
                "{\"to\":[{\"type\":\"phone\",\"number\":\"4477999000\"}],\"from\":{\"type\":\"phone\","
                        + "\"number\":\"44111222333\""
                        + "},\"answer_url\":[\"https://callback.example.com/\"],\"answer_method\":\"GET\"}",
                call.toJson()
        );
    }

    @Test
    public void testToJsonMachineDetection() throws Exception {
        Call call = new Call("4477999000", "44111222333", "https://callback.example.com/");
        call.setMachineDetection(MachineDetection.CONTINUE);
        assertEquals(
                "{\"to\":[{\"type\":\"phone\",\"number\":\"4477999000\"}],\"from\":{\"type\":\"phone\","
                        + "\"number\":\"44111222333\""
                        + "},\"answer_url\":[\"https://callback.example.com/\"],\"answer_method\":\"GET\",\"machine_detection\":\"continue\"}",
                call.toJson()
        );
    }

    @Test
    public void testSetters() throws Exception {
        PhoneEndpoint from = new PhoneEndpoint("44-AAA-FROM");
        PhoneEndpoint to = new PhoneEndpoint("44-BBB-TO");
        Call call = new Call("", "", "https://callback.example.com/");
        call.setAnswerMethod("BREW");
        call.setAnswerUrl("https://answer.example.com/");
        call.setEventMethod("RUN");
        call.setEventUrl("https://events.example.com/");
        call.setFrom(from);
        call.setLengthTimer(101);
        call.setMachineDetection(MachineDetection.CONTINUE);
        call.setRingingTimer(300);
        call.setTo(new PhoneEndpoint[]{to});

        assertEquals("BREW", call.getAnswerMethod());
        assertEquals("https://answer.example.com/", call.getAnswerUrl()[0]);
        assertEquals("RUN", call.getEventMethod());
        assertEquals("https://events.example.com/", call.getEventUrl()[0]);
        assertEquals(from, call.getFrom());
        assertEquals(101, call.getLengthTimer().intValue());
        assertEquals(MachineDetection.CONTINUE, call.getMachineDetection());
        assertEquals(300, call.getRingingTimer().intValue());
        assertEquals(to, call.getTo()[0]);
    }

    @Test
    public void testFromJson() {
        String jsonString = "{\"to\":" + "[{\"type\":\"phone\",\"number\":\"441632960960\"}],"
                + "\"from\":{\"type\":\"phone\",\"number\":\"441632960961\"},"
                + "\"answer_url\":\"http://example.com/answer\"}";

        Call newCall = new Call("441632960960", "441632960961", "http://example.com/answer");
        Call fromJson = Call.fromJson(jsonString);
        assertEquals(newCall.toJson(), fromJson.toJson());
    }

    @Test
    public void testMalformedJson() throws Exception {
        try {
            Call.fromJson("{\n" + "    \"unknownProperty\": \"unknown\"\n" + "}");
            fail("Expected a NexmoUnexpectedException to be thrown");
        } catch (NexmoUnexpectedException e) {
            assertEquals("Failed to produce json from Call object.", e.getMessage());
        }
    }

    @Test
    public void testNullAnswerMethodIfNoAnswerUrlDefined() {
        Call call = new Call();
        assertNull(call.getAnswerMethod());
    }

    @Test
    public void testGetAnswerUrlReturnsNullIfNotDefined() {
        Call call = new Call();
        assertNull(call.getAnswerUrl());
    }

    @Test
    public void testNccoParameterWithEmptyNcco() {
        Call call = new Call("15551234567", "25551234567", new Ncco());
        assertEquals(
                "{\"to\":[{\"type\":\"phone\",\"number\":\"15551234567\"}],\"from\":{\"type\":\"phone\",\"number\":\"25551234567\"},\"ncco\":[]}",
                call.toJson()
        );
    }

    @Test
    public void testNccoParameterWithSingleActionNcco() {
        Call call = new Call("15551234567", "25551234567", new Ncco(TalkAction.builder("Hello World").build()));
        assertEquals(
                "{\"to\":[{\"type\":\"phone\",\"number\":\"15551234567\"}],\"from\":{\"type\":\"phone\",\"number\":\"25551234567\"},\"ncco\":[{\"text\":\"Hello World\",\"action\":\"talk\"}]}",
                call.toJson()
        );
    }

    @Test
    public void testNccoParameterWithMultiActionNcco() {
        Call call = new Call("15551234567", "25551234567", new Ncco(
                TalkAction.builder("Hello World").build(),
                RecordAction.builder().build(),
                InputAction.builder().build(),
                TalkAction.builder("Goodbye").build()
        ));
        assertEquals(
                "{\"to\":[{\"type\":\"phone\",\"number\":\"15551234567\"}],\"from\":{\"type\":\"phone\",\"number\":\"25551234567\"},\"ncco\":[{\"text\":\"Hello World\",\"action\":\"talk\"},{\"action\":\"record\"},{\"action\":\"input\"},{\"text\":\"Goodbye\",\"action\":\"talk\"}]}",
                call.toJson()
        );
    }

}
