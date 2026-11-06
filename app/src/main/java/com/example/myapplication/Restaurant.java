package com.example.myapplication;

import java.io.Serializable;

public class Restaurant implements Serializable {
    private String id;
    private String name;
    private String type;
    private  String description;
    private  String location;
    private  String logoUrl;

    public Restaurant() {
    }

    public Restaurant(String id, String name, String type, String description, String location, String logoUrl) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.location = location;
        this.logoUrl = logoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @Override
    public String toString() {
        return "Ordar{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                '}';
    }
}

