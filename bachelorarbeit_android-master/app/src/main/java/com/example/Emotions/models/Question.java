package com.example.Emotions.models;

public class Question {
    private String Text;
    private String freeText;
    private int id;
    private float Antwort;

    public Question(String text, int id) {
        Text = text;
        this.id = id;
    }

    public String getFreeText() {
        return freeText;
    }

    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }

    public float getAntwort() {
        return Antwort;
    }

    public void setAntwort(float antwort) {
        Antwort = antwort;
    }

    public float getTest() {
        return Antwort;
    }

    public void setTest(float Antwort) {
        this.Antwort = Antwort;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "Question{" +
                "Text='" + Text + '\'' +
                ", id=" + id +
                ", Antwort='" + Antwort + '\'' +
                '}';
    }
}
