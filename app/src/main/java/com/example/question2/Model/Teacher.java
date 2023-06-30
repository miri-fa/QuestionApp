package com.example.question2.Model;

import java.util.ArrayList;
//Teacher user
public class Teacher extends User {
    private ArrayList <Questionnaire> questionnairesDone;
    private ArrayList <Team> teamsDone;

    public Teacher(String email, String password, String role) {
        super(email, password, role);
    }

    public void addQuestionnaire(Questionnaire questionnaire){
        questionnairesDone.add(questionnaire);
    }

    public void addTeam(Team team){
        teamsDone.add(team);
    }

    public ArrayList<Questionnaire> getQuestionnairesDone() {
        return questionnairesDone;
    }

    public void setQuestionnairesDone(ArrayList<Questionnaire> questionnairesDone) {
        this.questionnairesDone = questionnairesDone;
    }

    public ArrayList<Team> getTeamsDone() {
        return teamsDone;
    }

    public void setTeamsDone(ArrayList<Team> teamsDone) {
        this.teamsDone = teamsDone;
    }
}
