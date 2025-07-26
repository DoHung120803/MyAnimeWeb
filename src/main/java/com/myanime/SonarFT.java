package com.myanime;

public class SonarFT {
    public String sayHello(String name) {
        if (name == null || name.isEmpty()) {
            return "Hello, Stranger!";
        }
        return "Hello, " + name + "!";
    }
}
