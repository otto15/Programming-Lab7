package com.otto15.common.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Serializer {

    public Serializer() {

    }

    public byte[] serialize(Serializable objectToSerialize) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        objectOutput.writeObject(objectToSerialize);

        return byteArrayOutputStream.toByteArray();
    }

    public Serializable deserialize(byte[] serializedObject) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedObject);
        ObjectInputStream objectInput = new ObjectInputStream(byteArrayInputStream);
        try {
            return (Serializable) objectInput.readObject();
        } catch (ClassNotFoundException | IOException e) {
            return null;
        }
    }

}
