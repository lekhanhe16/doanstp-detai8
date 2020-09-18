package com.kl.doanstp.viewmodel.teammanage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kl.doanstp.model.Player;
import com.kl.doanstp.model.Team;

public class MyTeamViewModel extends ViewModel {

    private MutableLiveData<Player> mPlayer = new MutableLiveData<>();
    private MutableLiveData<Team> mTeam = new MutableLiveData<>();
    public void setPlayer(Player p){

        this.mPlayer.postValue(p);}

    public LiveData<Player> getPlayer() {
        return this.mPlayer;
    }

    public void setTeam(Team t){

        this.mTeam.postValue(t);
    }

    public LiveData<Team> getTeam() {
        return this.mTeam;
    }
}