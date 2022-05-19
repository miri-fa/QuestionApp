package com.example.question2.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ChoicesQuestion extends Question implements Serializable {
    private boolean multipleChoice;
    private ArrayList<String> choices;
    private ArrayList<String> answers;

    public ChoicesQuestion(){
        choices = new ArrayList<>();
        answers = new ArrayList<>();
        multipleChoice = false;
    }

    public void addChoice(String choice){
        choices.add(choice);
    }

    public void addAnswers(String answers){
        this.answers.add(answers);
    }

    public boolean isMultipleChoice() {
        return multipleChoice;
    }

    public void setMultipleChoice(boolean multipleChoice) {
        this.multipleChoice = multipleChoice;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = new ArrayList<>();
        this.answers = (ArrayList<String>) answers.clone();
    }
}
