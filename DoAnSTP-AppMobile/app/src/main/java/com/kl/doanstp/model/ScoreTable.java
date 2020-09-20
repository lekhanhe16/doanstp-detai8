package com.kl.doanstp.model;

import java.io.Serializable;

public class ScoreTable implements Serializable {
    private int id;
    private String finalScore, statistic;


    public ScoreTable(int id, String finalScore, String statistic) {
        this.id = id;
        this.finalScore = finalScore;
        this.statistic = statistic;

    }

    public ScoreTable() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(String finalScore) {
        this.finalScore = finalScore;
    }

    public String getStatistic() {
        return statistic;
    }

    public void setStatistic(String statistic) {
        this.statistic = statistic;
    }
}
