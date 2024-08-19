package com.studyhub.model;

public class Hut {

    private String name;
    private String type;

    // Default constructor
    public Hut() {}

    // Parameterized constructor
    public Hut(String name, String type) {
        this.name = name;
        this.type = type;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
