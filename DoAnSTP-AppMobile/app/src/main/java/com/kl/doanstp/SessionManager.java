package com.kl.doanstp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.kl.doanstp.model.Account;
import com.kl.doanstp.view.LoginActivity;
import com.kl.doanstp.view.MainActivity2;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;
    static SessionManager instance;
    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    private static final String ID = "ID";
    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";

//    public SessionManager(Context c){
//        this.context = c;
//        this.sharedPreferences = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
//        editor = sharedPreferences.edit();
//    }
    public SessionManager(){

    }
    public void setContext(Context context) {
        this.context = context;
        this.sharedPreferences = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public synchronized static SessionManager getInstance(){
        if(instance == null){
            instance = new SessionManager();
        }
        return instance;
    }
    public void createSession(int id, String username, String password){

        editor.putBoolean(LOGIN, true);
        editor.putInt(ID, id);
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.apply();
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(this.context, LoginActivity.class);
            this.context.startActivity(i);
            ((MainActivity2) context).finish();
        }
    }

    public Account getUser(){
        Account acc = new Account();
        acc.setId(sharedPreferences.getInt(ID, -1));
        acc.setPassWord(sharedPreferences.getString(PASSWORD, null));
        acc.setUserName(sharedPreferences.getString(USERNAME, null));
        return acc;
    }

    public void logOut(){
        this.editor.clear();
        this.editor.commit();
//        Intent i = new Intent(this.context, LoginActivity.class);
//        this.context.startActivity(i);
        ((MainActivity2) context).finish();
    }
}
