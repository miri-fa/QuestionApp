package com.example.question2.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class ChoicesQuestion extends Question{
    private boolean multipleChoice;
    private ArrayList<String> choices;
    private HashMap <Student,ArrayList<String>> answers;

    public ChoicesQuestion(){
        choices = new ArrayList<>();
        multipleChoice = false;
    }

    public void addChoice(String choice){
        choices.add(choice);
    }

    public void addAnswers(Student student, ArrayList<String> answer){
        this.answers.put(student,answer);
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

    public HashMap<Student, ArrayList<String>> getAnswers() {
        return answers;
    }

    public void setAnswers(HashMap<Student, ArrayList<String>> answers) {
        this.answers = answers;
    }
}
