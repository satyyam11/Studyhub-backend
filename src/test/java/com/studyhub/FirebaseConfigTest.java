package com.studyhub;

import com.google.cloud.firestore.Firestore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class FirebaseConfigTest {

    @Autowired
    private Firestore firestore;

    @Test
    public void testFirestoreBean() {
        assertNotNull(firestore, "Firestore bean should be initialized and not null");
    }
}

