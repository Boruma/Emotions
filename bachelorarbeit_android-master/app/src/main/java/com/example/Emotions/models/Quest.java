package com.example.Emotions.models;

public class Quest {
    private String id;
    private String Name;
    private String progress;
    private String target_id;
    private String point_user_id;

    public Quest() {
    }

    public Quest(String id, String name, String progress, String target_id) {
        this.id = id;
        Name = name;
        this.progress = progress;
        this.target_id = target_id;
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

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getTarget_id() {
        return target_id;
    }

    public void setTarget_id(String target_id) {
        this.target_id = target_id;
    }

    public String getPoint_user_id() {
        return point_user_id;
    }

    public void setPoint_user_id(String point_user_id) {
        this.point_user_id = point_user_id;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", Name='" + Name + '\'' +
                ", progress='" + progress + '\'' +
                ", target_id='" + target_id + '\'' +
                ", point_user_id='" + point_user_id + '\'' +
                '}';
    }
}
