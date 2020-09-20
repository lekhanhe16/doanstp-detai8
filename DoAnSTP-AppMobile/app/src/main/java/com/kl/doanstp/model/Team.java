package com.kl.doanstp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Team implements Serializable {
    private int id;
    private String name="", icon, phone;
    private double rating;
    private ArrayList<Player> players = new ArrayList<>();
    private int win, draw, lose;
    private boolean expanded;

    public Team() {
        this.name = "";
        this.expanded = false;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public Team(int id, String name, String icon, double rating, ArrayList<Player> players,
                int win, int draw, int lose, boolean expanded, TeamLocation tLocation) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.rating = rating;
        this.players = players;
        this.win = win;
        this.draw = draw;
        this.lose = lose;
        this.expanded = expanded;
        this.tLocation = tLocation;
        this.expanded = false;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    private TeamLocation tLocation;

    public TeamLocation gettLocation() {
        return tLocation;
    }

    public void settLocation(TeamLocation tLocation) {
        this.tLocation = tLocation;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
