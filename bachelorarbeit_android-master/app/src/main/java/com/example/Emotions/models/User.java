package com.example.Emotions.models;

public class User {
    private int id;
    private String name;
    private String email;
    private String education;
    private String sex;
    private String country;
    private int points;
    private int points_1;
    private int points_7;

    private String age;

    public User() {
    }

    public User(String name, String eMail, String education, String sex, String country, int points, String age) {
        name = name;
        this.email = eMail;
        this.education = education;
        this.sex = sex;
        this.country = country;
        this.points = points;
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPoints_1() {
        return points_1;
    }

    public void setPoints_1(int points_1) {
        this.points_1 = points_1;
    }

    public int getPoints_7() {
        return points_7;
    }

    public void setPoints_7(int points_7) {
        this.points_7 = points_7;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name;
    }

    public String geteMail() {
        return email;
    }

    public void seteMail(String eMail) {
        this.email = eMail;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", education='" + education + '\'' +
                ", sex='" + sex + '\'' +
                ", country='" + country + '\'' +
                ", points=" + points +
                ", points_1=" + points_1 +
                ", points_7=" + points_7 +
                ", age=" + age +
                '}';
    }
}
