package com.example.question2.Model;

import java.util.ArrayList;

public class Team {
    private String name;
    private Teacher creator;
    private ArrayList<Student> students;

    public void addStudents (Student student){
        this.students.add(student);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getCreator() {
        return creator;
    }

    public void setCreator(Teacher creator) {
        this.creator = creator;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }
}
