package com.sourcey.clouldcomputing;

public class crrDataHolder {
    private String name;
    private String id;
    private String description;

    public crrDataHolder(String name, String id, String description) {
        this.name = name;
        this.id = id;
        this.description = description;
    }

    public String toString() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
