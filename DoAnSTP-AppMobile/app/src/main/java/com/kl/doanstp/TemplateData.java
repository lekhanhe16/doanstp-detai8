package com.kl.doanstp;

import com.kl.doanstp.model.Account;
import com.kl.doanstp.model.DraftMatchTeam;
import com.kl.doanstp.model.DraftedMatch;
import com.kl.doanstp.model.Player;
import com.kl.doanstp.model.PlayerStatistic;
import com.kl.doanstp.model.ScoreTable;
import com.kl.doanstp.model.Team;
import com.kl.doanstp.model.TeamLocation;

import java.util.ArrayList;

public class TemplateData {
    static TemplateData instance;
    public  ArrayList<Account> arrAccount = new ArrayList<>();
    public  ArrayList<DraftedMatch> arrDraftMatch = new ArrayList<>();
    public  ArrayList<DraftMatchTeam> arrDraftedMatchTeams = new ArrayList<>();
//    public  ArrayList <FMatch> arrFMatch = new ArrayList<>();
    public  ArrayList<Player> arrPlayer = new ArrayList<>();
    public  ArrayList<PlayerStatistic> arrPlayerStat = new ArrayList<>();
    public  ArrayList<ScoreTable> arrScore = new ArrayList<>();
    public  ArrayList<Team> arrTeam = new ArrayList<>();
    public  ArrayList<TeamLocation> arrTeamLoc = new ArrayList<>();

    public TemplateData(){

    }

    public  void setArrAccount() {
        arrAccount.add(new Account(1,"khanh","123"));
        arrAccount.add(new Account(2,"duong","123"));
        arrAccount.add(new Account(3,"tung","123"));
        arrAccount.add(new Account(4,"son","123"));
        arrAccount.add(new Account(5,"bach","123"));
        arrAccount.add(new Account(6,"hieu","123"));
        arrAccount.add(new Account(7,"quang","123"));
        arrAccount.add(new Account(8,"vietanh","123"));
        arrAccount.add(new Account(9, "tien", "123"));
    }

    public  void setArrDraftMatch() {

    }

    public  void setArrDraftedMatchTeams() {
//        TemplateData.arrDraftedMatchTeams = arrDraftedMatchTeams;
    }

    public  void setArrFMatch() {
//        TemplateData.arrFMatch = arrFMatch;
    }

    public  void setarrPlayer() {
        ArrayList<Player> arrP1 = new ArrayList<>();

        Team team1 = new Team(1,"Sing7654","",5.0,arrP1 ,0,0,0,
                false,new TeamLocation(1, "Tran phu","Ha Dong", "Ha Noi",
                21.12,21.43));
        arrP1.add(new Player(1, 1998, "Khanh le","FW","","123456",
                true,new Account(1,"khanh","123"), team1));
        arrP1.add(new Player(2, 1998, "Duong le","MF","","123456",
                false,new Account(2,"duong","123"), team1));
        arrP1.add(new Player(3, 1998, "Tung Bui","DF","","123456",
                false,new Account(3,"tung","123"), team1));
        arrP1.add(new Player(4, 1998, "Bac Son","GK","","123456",
                false,new Account(4,"son","123"), team1));
        arrP1.clear();

        Team team2 = new Team(2,"Sing3210","",5.0,arrP1 ,0,0,0,
                false,new TeamLocation(2, "Pham Van Bach","Cau Giay", "Ha Noi",
                21.21,21.34));
        arrP1.add(new Player(5, 1998, "Bach Nguyen","FW","","123456",
                true,new Account(5,"bach","123"), team2));
        arrP1.add(new Player(6, 1998, "Hieu Nguyen","MF","","123456",
                false,new Account(6,"hieu","123"), team2));
        arrP1.add(new Player(7, 1998, "Quang Tran","DF","","123456",
                false,new Account(7,"quang","123"), team2));
        arrP1.add(new Player(8, 1998, "Viet Anh","GK","","123456",
                false,new Account(8,"vietanh","123"), team2));

        instance.arrPlayer.add(new Player(1, 1998, "Khanh le","FW","","123456",
                true,new Account(1,"khanh","123"), team1));
        instance.arrPlayer.add(new Player(2, 1998, "Duong le","MF","","123456",
                true,new Account(2,"duong","123"), team1));
        instance.arrPlayer.add(new Player(3, 1998, "Tung Bui","DF","","123456",
                true,new Account(3,"tung","123"), team1));
        instance.arrPlayer.add(new Player(4, 1998, "Bac Son","GK","","123456",
                true,new Account(4,"son","123"), team1));
        instance.arrPlayer.add(new Player(5, 1998, "Bach Nguyen","FW","","123456",
                true,new Account(5,"bach","123"), team2));
        instance.arrPlayer.add(new Player(6, 1998, "Hieu Nguyen","MF","","123456",
                false,new Account(6,"hieu","123"), team2));
        instance.arrPlayer.add(new Player(7, 1998, "Quang Tran","DF","","123456",
                false,new Account(7,"quang","123"), team2));
        instance.arrPlayer.add(new Player(8, 1998, "Viet Anh","GK","","123456",
                false,new Account(8,"vietanh","123"), team2));

        instance.arrPlayer.add(new Player(9, 1998, "Tien", "MF", "", "12345", false,
                new Account(9, "tien", "123"), null));

    }
    public static TemplateData getInstance(){
        if (instance == null){
            instance = new TemplateData();
            instance.setarrTeam();
            instance.setarrTeamLoc();
            instance.setArrAccount();
            instance.setarrPlayer();

        }
        return instance;
    }
    public  void setarrPlayerStat() {
//        TemplateData.instance.arrPlayerStat = instance.arrPlayerStat;
    }

    public  void setArrScore() {
//        TemplateData.arrScore = arrScore;
    }

    public  void setarrTeam() {
        ArrayList<Player> arrP1 = new ArrayList<>();

        Team team1 = new Team(1,"Sing7654","",5.0,arrP1 ,0,0,0,
                false,new TeamLocation(1, "Tran phu","Ha Dong", "Ha Noi",
                21.12,21.43));
        arrP1.add(new Player(1, 1998, "Khanh le","FW","","123456",
                true,new Account(1,"khanh","123"), team1));
        arrP1.add(new Player(2, 1998, "Duong le","MF","","123456",
                false,new Account(2,"duong","123"), team1));
        arrP1.add(new Player(3, 1998, "Tung Bui","DF","","123456",
                false,new Account(3,"tung","123"), team1));
        arrP1.add(new Player(4, 1998, "Bac Son","GK","","123456",
                false,new Account(4,"son","123"), team1));
        arrP1.clear();

        Team team2 = new Team(2,"Sing3210","",5.0,arrP1 ,0,0,0,
                false,new TeamLocation(2, "Pham Van Bach","Cau Giay", "Ha Noi",
                21.21,21.34));
        arrP1.add(new Player(5, 1998, "Bach Nguyen","FW","","123456",
                true,new Account(5,"bach","123"), team2));
        arrP1.add(new Player(6, 1998, "Hieu Nguyen","MF","","123456",
                false,new Account(6,"hieu","123"), team2));
        arrP1.add(new Player(7, 1998, "Quang Tran","DF","","123456",
                false,new Account(7,"quang","123"), team2));
        arrP1.add(new Player(8, 1998, "Viet Anh","GK","","123456",
                false,new Account(8,"vietanh","123"), team2));

        instance.arrTeam.add(team1);
        instance.arrTeam.add(team2);
    }

    public  void setarrTeamLoc() {
        TeamLocation teamLocation1 = new TeamLocation(1, "Tran phu","Ha Dong", "Ha Noi",
                21.12,21.43);
        TeamLocation teamLocation2 = new TeamLocation(2, "Pham Van Bach","Cau Giay", "Ha Noi",
                21.21,21.34);

        instance.arrTeamLoc.add(teamLocation1);
        instance.arrTeamLoc.add(teamLocation2);
    }
}
