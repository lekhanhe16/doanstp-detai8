package com.kl.doanstp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Player implements Serializable {
    private int id, birthYear;
    private String name, position, icon, phone;
    private boolean isCaptain;
    private Account account;
    private Team team;
    public Player(){
        this.team = new Team();
        this.icon = "";
    }
    public Player(int id, int birthYear, String name, String position, String icon,
                  String phone, boolean isCaptain, Account account, Team team) {
        this.id = id;
        this.birthYear = birthYear;
        this.name = name;
        this.position = position;
        this.icon = icon;
        this.phone = phone;
        this.isCaptain = isCaptain;
        this.account = account;
        this.team = team;
    }

//    protected Player(Parcel in) {
//        id = in.readInt();
//        birthYear = in.readInt();
//        name = in.readString();
//        position = in.readString();
//        icon = in.readString();
//        phone = in.readString();
//        isCaptain = in.readByte() != 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(id);
//        dest.writeInt(birthYear);
//        dest.writeString(name);
//        dest.writeString(position);
//        dest.writeString(icon);
//        dest.writeString(phone);
//        dest.writeByte((byte) (isCaptain ? 1 : 0));
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public static final Creator<Player> CREATOR = new Creator<Player>() {
//        @Override
//        public Player createFromParcel(Parcel in) {
//            return new Player(in);
//        }
//
//        @Override
//        public Player[] newArray(int size) {
//            return new Player[size];
//        }
//    };

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Team getTeam() {
        return this.team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isCaptain() {
        return isCaptain;
    }

    public void setCaptain(boolean captain) {
        isCaptain = captain;
    }
}
