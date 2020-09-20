package com.kl.doanstp.viewmodel.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kl.doanstp.model.Player;
import com.kl.doanstp.model.Team;

public class MapViewModel extends ViewModel {

    private MutableLiveData<Team> mTeam = new MutableLiveData<>();;
    private MutableLiveData<Player> mPlayer = new MutableLiveData<>();

    public void setmPlayer(Player p) {
        this.mPlayer.postValue(p);
    }

    public LiveData<Player> getPlayer() {
        return this.mPlayer;
    }

    public void setmTeam(Team t) {
        this.mTeam.postValue(t);
    }

    public LiveData<Team> getTeam() {
        return this.mTeam;
    }

}