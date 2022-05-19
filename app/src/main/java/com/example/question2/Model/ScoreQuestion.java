package com.example.question2.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ScoreQuestion extends Question implements Serializable {
    private String lowerSide, higherSide;
    private ArrayList<String> answers;

    public ScoreQuestion(){
        answers = new ArrayList<>();
    }

    public void addAnswers(String answer){
        this.answers.add(answer);
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

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }
}
