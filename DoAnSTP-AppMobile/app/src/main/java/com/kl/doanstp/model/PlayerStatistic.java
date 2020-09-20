package com.kl.doanstp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PlayerStatistic implements Serializable {
    private int id;
    private Player player = new Player();
    private int assist, block, goals, penalty, save, tackle;
    private boolean expanded;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public PlayerStatistic(int id, Player player, int assist, int block, int goals, int penalty, int save, int tackle) {
        this.id = id;
        this.player = player;
        this.assist = assist;
        this.block = block;
        this.goals = goals;
        this.penalty = penalty;
        this.save = save;
        this.tackle = tackle;
        this.expanded = false;
    }

    protected PlayerStatistic(Parcel in) {
        id = in.readInt();
        player = in.readParcelable(Player.class.getClassLoader());
        assist = in.readInt();
        block = in.readInt();
        goals = in.readInt();
        penalty = in.readInt();
        save = in.readInt();
        tackle = in.readInt();
    }

//    public static final Creator<PlayerStatistic> CREATOR = new Creator<PlayerStatistic>() {
//        @Override
//        public PlayerStatistic createFromParcel(Parcel in) {
//            return new PlayerStatistic(in);
//        }
//
//        @Override
//        public PlayerStatistic[] newArray(int size) {
//            return new PlayerStatistic[size];
//        }
//    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getAssist() {
        return assist;
    }

    public void setAssist(int assist) {
        this.assist = assist;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public int getSave() {
        return save;
    }

    public void setSave(int save) {
        this.save = save;
    }

    public int getTackle() {
        return tackle;
    }

    public void setTackle(int tackle) {
        this.tackle = tackle;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(id);
//        dest.writeParcelable(player, flags);
//        dest.writeInt(assist);
//        dest.writeInt(block);
//        dest.writeInt(goals);
//        dest.writeInt(penalty);
//        dest.writeInt(save);
//        dest.writeInt(tackle);
//    }
}
