package com.example.Emotions.models;

import android.os.Build;

import java.util.Date;
import java.util.Objects;

import androidx.annotation.RequiresApi;


public class Point {

    private int id;
    private String name;
    private String text;
    private int QRnumber;
    private double longitude;
    private double latitude;
    private Date created_at;
    private Date updated_at;
    private String ImageBase64;

    public Point() {
    }

    public Point(int id, String name, String text, int QRnumber, double longitude, double latitude, Date created_at, Date updated_at) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.QRnumber = QRnumber;
        this.longitude = longitude;
        this.latitude = latitude;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getQRnumber() {
        return QRnumber;
    }

    public void setQRnumber(int QRnumber) {
        this.QRnumber = QRnumber;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }


    @Override
    public String toString() {
        return "Point{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", QRnumber=" + QRnumber +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return id == point.id &&
                QRnumber == point.QRnumber &&
                Double.compare(point.longitude, longitude) == 0 &&
                Double.compare(point.latitude, latitude) == 0 &&
                name.equals(point.name) &&
                text.equals(point.text) &&
                created_at.equals(point.created_at) &&
                updated_at.equals(point.updated_at);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id, name, text, QRnumber, longitude, latitude, created_at, updated_at);
    }
}

