package com.example.question2.Model;

import java.util.HashMap;

public class ScoreQuestion extends Question{
    private String lowerSide, higherSide;
    private HashMap<Student,Integer> answers;

    public void addAnswers(Student student, Integer answer){
        this.answers.put(student,answer);
    }

    public String getLowerSide() {
        return lowerSide;
    }

    public void setLowerSide(String lowerSide) {
        this.lowerSide = lowerSide;
    }

    public String getHigherSide() {
        return higherSide;
    }

    public void setHigherSide(String higherSide) {
        this.higherSide = higherSide;
    }

    public HashMap<Student, Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(HashMap<Student, Integer> answers) {
        this.answers = answers;
    }
}
