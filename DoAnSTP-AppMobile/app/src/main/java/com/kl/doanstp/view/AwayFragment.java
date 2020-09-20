package com.kl.doanstp.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.stats.ConnectionTracker;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kl.doanstp.R;
import com.kl.doanstp.adapter.MatchControlDataLoad;
import com.kl.doanstp.adapter.RecyclerAwayAdapter;
import com.kl.doanstp.model.DraftMatchTeam;
import com.kl.doanstp.model.DraftedMatch;
import com.kl.doanstp.model.Player;
import com.kl.doanstp.model.ScoreTable;
import com.kl.doanstp.model.Team;
import com.kl.doanstp.model.TeamLocation;
import com.kl.doanstp.service.ConnectionUtil;
import com.kl.doanstp.service.MyService;
import com.kl.doanstp.service.RetrofitClient;
import com.kl.doanstp.viewmodel.host.AwayViewModel;
import com.kl.doanstp.viewmodel.host.HostViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AwayFragment extends Fragment {
    private RecyclerView listInviter;
    private RecyclerAwayAdapter adapter;
    private ArrayList<DraftedMatch> arrDM = new ArrayList<>();
    private static String baseURL = ConnectionUtil.baseURL;
    MatchControlDataLoad mcdl;
    private Team myTeam;
    private Player thisplayer;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcdl = (MatchControlDataLoad) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_away_fragment, container, false);
        myTeam = mcdl.onGetTeamData();
        thisplayer = mcdl.onGetPlayerData();
        listInviter = v.findViewById(R.id.away_list_inviter);
        String query = "SELECT lefttable.*, scoretable.id, finalscore, statistic " +
                "FROM (SELECT team.Id as tid, team.Name as tname, team.Rating as trating, team.phone as tphone, team.Icon as ticon, " +
                "team.win as win, team.draw as draw, team.lose as lose, " +
                "draftedmatch.Id as dmid, day, place, time, " +
                "draftedmatch_team.teamid as dmteamid, draftedmatch_team.role, draftedmatch_team.isready, " +
                "isaccepted, isdenied, isend, " +
                "draftedmatch_team.resstt " +
                "FROM team, draftedmatch, draftedmatch_team," +

                "(SELECT draftedmatch_team.draftedmatchid as dmaid FROM draftedmatch, draftedmatch_team " +
                "WHERE draftedmatch.Id = draftedmatch_team.draftedmatchid " +
                "AND draftedmatch_team.teamid = " + myTeam.getId()+
                " AND draftedmatch.IsDenied = false " +
                " AND (draftedmatch_team.role = 'invited' " +
                "OR draftedmatch_team.role = 'guest')" +
                ") AS awaymatch " +
                "WHERE team.Id = draftedmatch_team.teamid " +
                "AND draftedmatch_team.draftedmatchid = draftedmatch.Id AND awaymatch.dmaid = draftedmatch.Id " +

                ") as lefttable " +
                "left join scoretable " +
                "on lefttable.dmid = scoretable.draftedmatchid " +
                "ORDER BY lefttable.dmid DESC, role ASC";;

        JSONObject data = new JSONObject();
        try {
            data.put("query", query);
            MyService myRetrofit = RetrofitClient.getInstance(baseURL).create(MyService.class);
            Call<JsonObject> callBack = myRetrofit.searchForInvitedMatches(data);
            callBack.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.isSuccessful()){
                        arrDM.clear();

                        JsonArray arrResult = response.body().get("result").getAsJsonArray();
                        for (JsonElement e : arrResult) {
                            JsonObject team = e.getAsJsonObject().get("team").getAsJsonObject();
                            Team team1 = new Team(
                                    team.get("tid").getAsInt(),
                                    team.get("tname").getAsString(),
                                    team.get("ticon").getAsString(),
                                    team.get("trating").getAsDouble(),
                                    new ArrayList<Player>(),
                                    team.get("win").getAsInt(),
                                    team.get("draw").getAsInt(),
                                    team.get("lose").getAsInt(), false, new TeamLocation()
                            );

                            JsonArray teamPlayers = e.getAsJsonObject().get("players").getAsJsonArray();
                            for (JsonElement o : teamPlayers){
                                Player fet = new Player(o.getAsJsonObject().get("id").getAsInt(),
                                        o.getAsJsonObject().get("birthyear").getAsInt(),
                                        o.getAsJsonObject().get("name").getAsString(),
                                        o.getAsJsonObject().get("position").getAsString(),
                                        o.getAsJsonObject().get("icon").getAsString(),
                                        o.getAsJsonObject().get("phone").getAsString(),
                                        o.getAsJsonObject().get("isCaptain").getAsBoolean(),
                                        null, team1);
                                team1.getPlayers().add(fet);
                            }
                            JsonObject match = e.getAsJsonObject().get("match").getAsJsonObject();
                            ScoreTable sct = new ScoreTable();

                            try {
                                if (match.get("scoreid").getAsInt()>0){
                                    sct = new ScoreTable(match.get("scoreid").getAsInt(),
                                            match.get("finalscore").getAsString(), match.get("stats").getAsString());
                                }
                            }
                            catch (UnsupportedOperationException e1){

                            }
                            DraftedMatch dm = new DraftedMatch(
                                    match.get("dmid").getAsInt(),
                                    match.get("isaccept").getAsBoolean(), match.get("isdenied").getAsBoolean()
                                    , match.get("place").getAsString(),
                                    java.sql.Date.valueOf(match.get("day").getAsString()),
                                    match.get("time").getAsString(),
                                    match.get("isend").getAsBoolean()
                            );
                            dm.setScoreTable(sct);
                            DraftMatchTeam dmt = new DraftMatchTeam(team1, dm, match.get("role").getAsString(),
                                    match.get("isready").getAsBoolean(), match.get("resstt").getAsInt());
                            if (arrDM.size() > 0) {
                                if (dmt.getRole().equalsIgnoreCase("invite") ||
                                dmt.getRole().equalsIgnoreCase("invited")){
                                    if (dmt.getdMatch().getId() == arrDM.get(arrDM.size() - 1).getId()) {
                                        arrDM.get(arrDM.size() - 1).getDmt().add(dmt);
                                    } else {
                                        dm.getDmt().add(dmt);
                                        arrDM.add(dm);
                                    }
                                }
                                else {
                                    if (dmt.getdMatch().getId() == arrDM.get(arrDM.size() - 1).getId()) {
                                        if (dmt.getRole().equalsIgnoreCase("guest") &&
                                        dmt.getTeam().getId() == myTeam.getId()){
                                            arrDM.get(arrDM.size() - 1).getDmt().add(dmt);
                                        }

                                    } else {
                                        dm.getDmt().add(dmt);
                                        arrDM.add(dm);
                                    }
                                }
                            } else {
                                dm.getDmt().add(dmt);
                                arrDM.add(dm);
                            }
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new RecyclerAwayAdapter(getContext(), arrDM, myTeam, thisplayer);
                                listInviter.setAdapter(adapter);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                                        LinearLayoutManager.VERTICAL, false);
                                listInviter.setLayoutManager(linearLayoutManager);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
