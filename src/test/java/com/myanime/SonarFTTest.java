package com.myanime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SonarFTTest {

    @Test
    void testSayHello() {
        SonarFT service = new SonarFT();
        assertEquals("Hello, Stranger!", service.sayHello(null));
        assertEquals("Hello, Stranger!", service.sayHello(""));
        assertEquals("Hello, John!", service.sayHello("John"));
    }
}
