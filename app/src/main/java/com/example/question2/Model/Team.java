package com.example.question2.Model;

import java.util.ArrayList;

public class Team {
    private String name;
    private String creator;
    private ArrayList<String> students;
    private String accessCode;

    public void setAccessCode (String code){ this.accessCode = code;}

    public String getAccessCode (){return accessCode;}

    public void addStudents (String student){
        this.students.add(student);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public ArrayList<String> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<String> students) {
        this.students = students;
    }
}
