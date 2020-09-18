package com.kl.doanstp.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kl.doanstp.R;
import com.kl.doanstp.TemplateData;
import com.kl.doanstp.adapter.MatchControlDataLoad;
import com.kl.doanstp.adapter.RCVHostAdapter;
import com.kl.doanstp.model.DraftMatchTeam;
import com.kl.doanstp.model.DraftedMatch;
import com.kl.doanstp.model.Player;
import com.kl.doanstp.model.ScoreTable;
import com.kl.doanstp.model.Team;
import com.kl.doanstp.model.TeamLocation;
import com.kl.doanstp.service.ConnectionUtil;
import com.kl.doanstp.service.MyService;
import com.kl.doanstp.service.RetrofitClient;
import com.kl.doanstp.viewmodel.host.HostViewModel;
import com.kl.doanstp.viewmodel.map.MapViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostFragment extends Fragment {
    RecyclerView viewMatches;
    RCVHostAdapter adapter;
    Player thisPlayer = new Player();
    private Team myTeam;
    private ArrayList<Team> arrTeams = new ArrayList<>();
    private ArrayList<DraftedMatch> arrDM = new ArrayList<>();
    private static String baseURL = ConnectionUtil.baseURL;


    MatchControlDataLoad mcdl;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcdl = (MatchControlDataLoad) context;
    }

    public HostFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_host_fragment, container, false);
        myTeam = mcdl.onGetTeamData();
        thisPlayer = mcdl.onGetPlayerData();
        viewMatches = v.findViewById(R.id.rcv_host);

        String query = "SELECT lefttable.*, scoretable.id, finalscore, statistic " +
                "FROM (SELECT team.id as tid, team.name as tname, team.rating as trating, " +
                "team.phone as tphone, team.icon as ticon, team.win, team.draw, team.lose, " +
                "draftedmatch_team.draftedmatchid as dmid, day, place,time, " +
                "draftedmatch_team.teamid as dmteamid, role, " +
                "draftedmatch_team.isready, isaccepted, isdenied, isend, " +
                "draftedmatch_team.resstt " +
//                ",scoretable.id, finalscore, statistic "+
                "FROM " +
                "(SELECT team.id, team.name, team.rating, team.phone, team.icon, team.win, team.draw, " +
                "team.lose, draftedmatch.Id as dmid " +
                "FROM team, draftedmatch, draftedmatch_team " +
                "WHERE draftedmatch_team.teamid = team.Id AND team.id = "+ myTeam.getId()+
                " AND draftedmatch_team.draftedmatchid = draftedmatch.Id " +
                "AND NOT role = 'invited' AND NOT role='guest'" +
                ") AS teammatch, draftedmatch_team, draftedmatch, team " +
//                ",scoretable " +
                "WHERE teammatch.dmid = draftedmatch.Id AND draftedmatch_team.draftedmatchid = draftedmatch.Id " +
                "and team.Id = draftedmatch_team.teamid " +
//                "AND teammatch.dmid = scoretable.draftedmatchid " +
                ") as lefttable " +
                "left join scoretable " +
                "on lefttable.dmid = scoretable.draftedmatchid " +
                "ORDER BY lefttable.dmid DESC, role ASC, isready DESC";

        JSONObject data = new JSONObject();
        try {
            data.put("query", query);
            MyService myRetrofit = RetrofitClient.getInstance(baseURL).create(MyService.class);
            Call<JsonObject> callBack = myRetrofit.searchForHotMatches(data);

            callBack.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        arrTeams.clear();
                        arrDM.clear();

                        JsonArray arrResult = response.body().get("result").getAsJsonArray();
                        for (JsonElement e : arrResult) {
//                            JsonObject teamlocation = e.getAsJsonObject().get("teamlocation").getAsJsonObject();
//                            TeamLocation tl = new TeamLocation(teamlocation.get("tlid").getAsInt(),
//                                    teamlocation.get("street").getAsString(),
//                                    teamlocation.get("district").getAsString(),
//                                    teamlocation.get("city").getAsString(),
//                                    teamlocation.get("latitude").getAsDouble(),
//                                    teamlocation.get("longtitude").getAsDouble());
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
                                    match.get("isaccept").getAsBoolean(),
                                    match.get("isdenied").getAsBoolean(), match.get("place").getAsString(),
                                    java.sql.Date.valueOf(match.get("day").getAsString()),
                                    match.get("time").getAsString(),
                                    match.get("isend").getAsBoolean()
                            );
                            dm.setScoreTable(sct);
                            DraftMatchTeam dmt = new DraftMatchTeam(team1, dm, match.get("role").getAsString(),
                                    match.get("isready").getAsBoolean(),
                                    match.get("resstt").getAsInt());

                            if (arrDM.size() > 0) {
                                if (dmt.getdMatch().getId() == arrDM.get(arrDM.size() - 1).getId()) {
                                    arrDM.get(arrDM.size() - 1).getDmt().add(dmt);
                                } else {
                                    dm.getDmt().add(dmt);
                                    arrDM.add(dm);
                                }
                            } else {
                                dm.getDmt().add(dmt);
                                arrDM.add(dm);
                            }

                        }
//                        for (int i = 0; i < arrDM.size(); i++) {
//                            if (arrDM.get(i).getDmt().get(0).getTeam().getId() != myTeam.getId()) {
//                                arrDM.remove(i);
//                            }
//                        }

                        for (DraftedMatch d : arrDM) {
                            if (d.getDmt().get(0).getRole().equalsIgnoreCase("create")) {
                                d.getDmt().remove(0);
                            }
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new RCVHostAdapter(getContext(), arrDM, thisPlayer, myTeam);
                                viewMatches.setAdapter(adapter);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                                        LinearLayoutManager.VERTICAL, false);
                                viewMatches.setLayoutManager(linearLayoutManager);
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