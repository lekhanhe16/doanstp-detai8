package com.kl.doanstp.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;


public class DraftedMatch implements Serializable {
    private int id;
    private boolean isAccepted, isDenied, isEnd;
    private String place;
    private Date day;
    private String time;
    private boolean expanded = false;
    private ScoreTable scoreTable;
    private ArrayList<DraftMatchTeam> dmt = new ArrayList<>();

    public ScoreTable getScoreTable() {
        return scoreTable;
    }

    public void setScoreTable(ScoreTable scoreTable) {
        this.scoreTable = scoreTable;
    }

    public DraftedMatch() {
    }

    public DraftedMatch(int id, boolean isAccepted, boolean isDenied, String place, Date day, String time,
                        boolean isEnd) {
        this.id = id;
        this.isAccepted = isAccepted;
        this.isDenied = isDenied;
        this.place = place;
        this.day = day;
        this.time = time;
        this.isEnd = isEnd;
        this.scoreTable = new ScoreTable();
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public boolean isDenied() {
        return isDenied;
    }

    public void setDenied(boolean denied) {
        isDenied = denied;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<DraftMatchTeam> getDmt() {
        return dmt;
    }

    public void setDmt(ArrayList<DraftMatchTeam> dmt) {
        this.dmt = dmt;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

}
