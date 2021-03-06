package com.example.kcb;

import android.util.Log;

import java.io.Serializable;

public class Course implements Serializable {

    private String dsz;
    private String courseName;
    private String teacher;
    private String classRoom;
    private String homework;
    private int day;
    private int classStart;
    private int classEnd;

    public Course(String courseName, String teacher, String classRoom, int day, int classStart, int classEnd, String dsz, String homework) {
        this.courseName = courseName;
        this.teacher = teacher;
        this.classRoom = classRoom;
        this.day = day;
        this.classStart = classStart;
        this.classEnd = classEnd;
        this.dsz = dsz;
        this.homework = homework;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStart() {
        return classStart;
    }

    public void setStart(int classStart) {
        this.classEnd = classStart;
    }

    public int getEnd() {
        return classEnd;
    }

    public void setEnd(int classEnd) {
        this.classEnd = classEnd;
    }

    public String getDsz() {
        return dsz;
    }

    public void setDsz(String dsz) {
        this.dsz = dsz;
    }

    public void setHomework(String homework){
        this.homework = homework;
    }

    public String getHomework(){
        return homework;
    }

}
