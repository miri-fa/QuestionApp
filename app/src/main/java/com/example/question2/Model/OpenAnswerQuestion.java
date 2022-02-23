package com.example.question2.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class OpenAnswerQuestion extends Question{
    private HashMap<Student,String> answers;

    public HashMap<Student, String> getAnswers() {
        return answers;
    }

    public void addAnswers(Student student, String answer){
        this.answers.put(student,answer);
    }

    public void setAnswers(HashMap<Student, String> answers) {
        this.answers = answers;
    }
}
