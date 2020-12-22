package com.example.Emotions.models;

public class Target {
   private String id;
   private String Name;
   private String user_id;

    public Target() {
    }

    public Target(String id, String name, String user_id) {
        this.id = id;
        Name = name;
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", Name='" + Name + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}


