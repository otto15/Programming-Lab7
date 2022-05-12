package com.otto15.common.network;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SerializerTest {

    @Test
    void serialize() throws IOException {
        String s = "dj khaled \n another one";
        String newS = (String) Serializer.deserialize(Serializer.serialize(s));
        assertNotEquals(s + "1", newS);
    }

    @Test
    void wrongSerialize() throws IOException {
        String s = "dj khaled \n another one";
        String newS = (String) Serializer.deserialize(Serializer.serialize(s));
        assertEquals(s, newS);
    }

}