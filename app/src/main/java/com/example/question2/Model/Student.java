package com.example.question2.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Student extends User implements Serializable {
    private ArrayList<Questionnaire> questionnairesForMe;
    private ArrayList<Team> teamsForMe;

    public Student(String email, String password, String role) {
        super(email, password, role);
    }

    public void addQuestionnaire(Questionnaire questionnaire){
        questionnairesForMe.add(questionnaire);
    }

    public void addTeam(Team team){
        teamsForMe.add(team);
    }

    public ArrayList<Questionnaire> getQuestionnairesForMe() {
        return questionnairesForMe;
    }

    public void setQuestionnairesForMe(ArrayList<Questionnaire> questionnairesForMe) {
        this.questionnairesForMe = questionnairesForMe;
    }

    public ArrayList<Team> getTeamsForMe() {
        return teamsForMe;
    }

    public void setTeamsForMe(ArrayList<Team> teamsForMe) {
        this.teamsForMe = teamsForMe;
    }
}
