package com.kl.doanstp.model;

import java.io.Serializable;

public class DraftMatchTeam implements Serializable {
    private Team team;
    private DraftedMatch dMatch;
    private String role;
    private int resstt;
    boolean isReady;
    public DraftMatchTeam(Team team, DraftedMatch dMatch, String role, boolean accpt, int resstt) {
        this.team = team;
        this.dMatch = dMatch;
        this.role = role;
        this.isReady = accpt;
        this.resstt = resstt;
    }

    public int getResstt() {
        return resstt;
    }

    public void setResstt(int resstt) {
        this.resstt = resstt;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean accepted) {
        isReady = accepted;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public DraftedMatch getdMatch() {
        return dMatch;
    }

    public void setdMatch(DraftedMatch dMatch) {
        this.dMatch = dMatch;
    }
}
