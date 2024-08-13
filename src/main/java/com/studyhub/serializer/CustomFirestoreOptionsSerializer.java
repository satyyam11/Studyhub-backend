package com.studyhub.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.cloud.firestore.FirestoreOptions;

import java.io.IOException;

public class CustomFirestoreOptionsSerializer extends JsonSerializer<FirestoreOptions> {

    @Override
    public void serialize(FirestoreOptions firestoreOptions, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeStartObject();
        // Serialize only the necessary fields
        gen.writeStringField("projectId", firestoreOptions.getProjectId());
        // Add other fields as needed
        gen.writeEndObject();
    }
}
