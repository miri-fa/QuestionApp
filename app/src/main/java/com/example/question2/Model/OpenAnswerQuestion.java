package com.example.question2.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
//Class of a question with an open answer
public class OpenAnswerQuestion extends Question implements Serializable {
    private ArrayList<String> answers;

    public OpenAnswerQuestion(){
        answers = new ArrayList<>();
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void addAnswers(String answer){
        this.answers.add(answer);
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = new ArrayList<>();
        this.answers = (ArrayList<String>) answers.clone();
    }

    public void addAnswer(String answer) {
        this.answers.add(answer);
    }
}
