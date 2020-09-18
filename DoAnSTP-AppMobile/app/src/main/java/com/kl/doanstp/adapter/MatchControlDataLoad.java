package com.kl.doanstp.adapter;

import com.kl.doanstp.model.Player;
import com.kl.doanstp.model.Team;

public interface MatchControlDataLoad {
    Team onGetTeamData();
    Player onGetPlayerData();
}
