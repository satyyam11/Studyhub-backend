package com.studyhub.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.cloud.firestore.DocumentSnapshot;

import java.io.IOException;

public class FirestoreSerializer extends JsonSerializer<DocumentSnapshot> {

    @Override
    public void serialize(DocumentSnapshot documentSnapshot, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", documentSnapshot.getId());
        // Serialize other fields as necessary
        jsonGenerator.writeEndObject();
    }
}
