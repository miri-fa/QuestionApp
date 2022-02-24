package com.example.question2.Model;

import com.example.question2.Model.Question;
import com.example.question2.Model.Student;
import com.example.question2.Model.Teacher;

import java.util.ArrayList;

public class Questionnaire {
    private String title;
    private boolean published;
    private ArrayList <Student> doneBy;
    private String author;
    private ArrayList <Question> questions;

    public String getTitle() {
        return title;
    }

    public Questionnaire(){
        questions = new ArrayList<>();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addStudent(Student student){
        doneBy.add(student);
    }

    public void addQuestion(Question question){
        questions.add(question);
    }

    public void deleteQuestion (Question question) {questions.remove(question);}

    public boolean isAtEndOfArray (int count){
        boolean atEnd = false;
        if (questions.size() == count){
            atEnd = true;
        }
        return atEnd;
    }

    public Question getQuestionFromPosition (int pos){
        Question question;
        question = questions.get(pos);
        return question;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public ArrayList<Student> getDoneBy() {
        return doneBy;
    }

    public void setDoneBy(ArrayList<Student> doneBy) {
        this.doneBy = doneBy;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = (ArrayList<Question>) questions.clone();
    }
}
