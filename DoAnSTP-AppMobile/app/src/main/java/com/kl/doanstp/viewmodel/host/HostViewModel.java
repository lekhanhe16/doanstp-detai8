package com.kl.doanstp.viewmodel.host;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kl.doanstp.model.Player;
import com.kl.doanstp.model.Team;

public class HostViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private MutableLiveData<Team> hTeam = new MutableLiveData<>();;
    private MutableLiveData<Player> hPlayer = new MutableLiveData<>();

    public void sethPlayer(Player p) {
        this.hPlayer.postValue(p);
    }

    public LiveData<Player> getPlayer() {
        return this.hPlayer;
    }

    public void sethTeam(Team t) {
        this.hTeam.postValue(t);
    }

    public LiveData<Team> getTeam() {
        return this.hTeam;
    }
}