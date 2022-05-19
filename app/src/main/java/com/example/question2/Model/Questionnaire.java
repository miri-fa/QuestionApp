package com.example.question2.Model;

import com.example.question2.Model.Question;
import com.example.question2.Model.Student;
import com.example.question2.Model.Teacher;

import java.io.Serializable;
import java.util.ArrayList;

public class Questionnaire implements Serializable {
    private String title;
    private boolean published;
    private ArrayList <Student> doneBy;
    private String code;
    private String author;
    private ArrayList <Question> questions;
    private ArrayList<String> studentsAssgined;
    private ArrayList<String> teamsAssigned;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public Questionnaire(){
        questions = new ArrayList<>();
    }

    public int size(){return questions.size();}

    public void setTitle(String title) {
        this.title = title;
    }

    public void addStudent(Student student){
        doneBy.add(student);
    }

    public void addQuestion(Question question){
        questions.add(question);
    }

    public void deleteQuestion (int index) {questions.remove(index);}

    public boolean isAtEndOfArray (int count){
        boolean atEnd = false;
        if (questions.size() == count){
            atEnd = true;
        }
        return atEnd;
    }

    public Question getQuestionFromPosition (int pos){
        return questions.get(pos);
    }

    public void insertQuestion (int index, Question q){
        questions.add(index, q);
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public void setQuestion(int position, Question question){
        questions.set(position,question);
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
        this.questions = new ArrayList<>();
        this.questions = (ArrayList<Question>) questions.clone();
    }
    public ArrayList<String> getStudentsAssgined() {
        return studentsAssgined;
    }

    public void setStudentsAssgined(ArrayList<String> students) {
        this.studentsAssgined = new ArrayList<>();
        this.studentsAssgined = (ArrayList<String>) students.clone();
    }
    public ArrayList<String> getTeamsAssigned() {
        return teamsAssigned;
    }

    public void setTeamsAssigned(ArrayList<String> teams) {
        this.teamsAssigned = new ArrayList<>();
        this.teamsAssigned = (ArrayList<String>) teams.clone();
    }
    public void addStudentAssigned (int index, String s){
        studentsAssgined.add(index, s);
    }
    public void addTeamAssigned (int index, String s){
        teamsAssigned.add(index, s);
    }
}
