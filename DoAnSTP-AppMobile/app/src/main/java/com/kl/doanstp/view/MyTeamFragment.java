package com.kl.doanstp.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kl.doanstp.R;
import com.kl.doanstp.SessionManager;
import com.kl.doanstp.adapter.RecyclerViewPlayerAdapter;
import com.kl.doanstp.adapter.RecyclerViewTeamAdapter;
import com.kl.doanstp.model.Account;
import com.kl.doanstp.model.Player;
import com.kl.doanstp.model.PlayerStatistic;
import com.kl.doanstp.model.Team;
import com.kl.doanstp.model.TeamLocation;
import com.kl.doanstp.service.ConnectionUtil;
import com.kl.doanstp.service.MyService;
import com.kl.doanstp.service.RetrofitClient;

import com.kl.doanstp.viewmodel.map.MapViewModel;
import com.kl.doanstp.viewmodel.teammanage.MyTeamViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyTeamFragment extends Fragment {
    private final int NO_TEAM = 1;
    private final int TEAM_CAPTAIN = 2;
    private final int TEAM_MEMBER = 3;
    private MyTeamViewModel myTeamViewModel;
    private MapViewModel mapViewModel;
    private RecyclerView viewFW, viewMF, viewDF, viewGK, viewTeam;
    private RecyclerViewPlayerAdapter adapterFW, adapterMF, adapterDF, adapterGK;
    private RecyclerViewTeamAdapter adapterApply;
    private ArrayList<Player> playersFW = new ArrayList<>();
    private ArrayList<Player> playersMF = new ArrayList<>();
    private ArrayList<Player> playersDF = new ArrayList<>();
    private ArrayList<Player> playersGK = new ArrayList<>();
    private ArrayList<Team> teams = new ArrayList<>();
    volatile boolean taskRunning;
    private static String baseURL = ConnectionUtil.baseURL;
    private Player pl;
    private Team myTeam = new Team();
    private boolean newAppl = false;
    private static TextView numOfCen;
    private Account user;
    PlayerInfoTask task;
    public static MyTeamFragment mtf;
    private ArrayList<Integer> arrApply = new ArrayList<>();
    MyTeamFrgmUiUpdate uiUpdate;
    class MyTeamFrgmUiUpdate extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("UPDATE_CEN_NUM")){
                newAppl = true;
                numOfCen.setVisibility(View.VISIBLE);
            }

        }
    }
    public interface OnDataLoad {
        void loadPlayerData(Player player, Team team);
    }

    OnDataLoad dataLoader;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        taskRunning = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        taskRunning = false;
        getActivity().unregisterReceiver(uiUpdate);
    }

    public static void updateUI(){

    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mtf = MyTeamFragment.this;
        SessionManager.getInstance().checkLogin();
        user = SessionManager.getInstance().getUser();
        Log.d("user", user.getUserName() + " " + user.getPassWord() + " " + user.getId());

        pl = new Player();
        int res = 0;

        task = new PlayerInfoTask();

        try {
            res = task.execute().get();

            Log.d("case", res + "");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        myTeamViewModel.setPlayer(pl);
        View root = null;
        if (res == NO_TEAM) {
            Log.d("chua vao tem", "true");
            root = inflater.inflate(R.layout.fragment_apply, container, false);
            viewTeam = root.findViewById(R.id.recycler_list_apply);
            Button btnCreateTeam = root.findViewById(R.id.btn_create_team);
            btnCreateTeam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = (LayoutInflater)
                            getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popup_create_new_tem, null);
                    // create the popup window
                    int width = LinearLayout.LayoutParams.MATCH_PARENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                    popupWindow.showAtLocation(v, Gravity.CENTER_VERTICAL, 0, 0);

                    ImageView temIcon = popupView.findViewById(R.id.cre_tem_icon);
                    EditText creName, creSDT, creStreet, creDist, creCity;
                    Button btnCreate = popupView.findViewById(R.id.btn_cre_submit);
                    creCity = popupView.findViewById(R.id.edit_tp);
                    creDist = popupView.findViewById(R.id.edit_quan);
                    creStreet = popupView.findViewById(R.id.edit_duong);

                    creName = popupView.findViewById(R.id.edit_cre_temname);
                    creSDT = popupView.findViewById(R.id.edit_cre_phone);
                    String teamIcon = "";
                    btnCreate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String teamName = creName.getText().toString();
                            String teamPhone = creSDT.getText().toString();
                            String street = creStreet.getText().toString();
                            String dist = creDist.getText().toString();
                            String city = creCity.getText().toString();

                            if (!teamName.isEmpty()
                                    && !teamPhone.isEmpty()
                                    && !street.isEmpty()
                                    && !dist.isEmpty()
                                    && !city.isEmpty()) {

                                try {
                                    JSONObject data = new JSONObject();
                                    data.put("teamname", teamName);
                                    data.put("phone", teamPhone);
                                    data.put("street", street.toLowerCase());
                                    data.put("district", dist.toLowerCase());
                                    data.put("city", city.toLowerCase());
                                    data.put("icon", teamIcon);
                                    data.put("accountid", user.getId());

                                    MyService myRetrofit = RetrofitClient.getInstance(baseURL).create(MyService.class);
                                    Call<JsonObject> homeCallback = myRetrofit.createNewTeam(data);

                                    homeCallback.enqueue(new Callback<JsonObject>() {
                                        @Override
                                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                            int teamId = response.body().get("result").getAsInt();
                                            if (teamId != -1) {
                                                getFragmentManager()
                                                        .beginTransaction()
                                                        .detach(mtf)
                                                        .attach(mtf)
                                                        .commit();
                                            } else {
                                                Toast.makeText(getContext(),
                                                        "Ten doi bong da duoc su dung. Ban hay chon ten khac",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<JsonObject> call, Throwable t) {

                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            popupWindow.dismiss();
                        }
                    });
                    temIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            });

            adapterApply = new RecyclerViewTeamAdapter(teams, pl, getContext(), arrApply);
            viewTeam.setAdapter(adapterApply);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false);
            viewTeam.setLayoutManager(linearLayoutManager);
        } else {
            if (res == TEAM_CAPTAIN) {

                root = inflater.inflate(R.layout.fragment_team_captain, container, false);
                Log.d("my team size", playersFW.size() + "");
                initViewFW(root);
                initViewMF(root);
                initViewDF(root);
                initViewGK(root);
                TextView teamManage, teamCensor, teamBreak, teamName;
                numOfCen = root.findViewById(R.id.btn_cen_cap);
                numOfCen.setVisibility(View.GONE);
                teamCensor = root.findViewById(R.id.btn_duyet_tv_cap);
                teamName = root.findViewById(R.id.team_name_cap);
                teamName.setText(myTeam.getName());
                if (!newAppl){
                    numOfCen.setVisibility(View.GONE);
                }
                else{
                    numOfCen.setVisibility(View.VISIBLE);
                }
                teamCensor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject data = new JSONObject();
                        newAppl = false;
                        numOfCen.setVisibility(View.GONE);
                        try {
                            data.put("teamid", myTeam.getId());
                            MyService myRetrofit = RetrofitClient.getInstance(baseURL).create(MyService.class);
                            Call<JsonObject> calBack = myRetrofit.getApplicants(data);
                            calBack.enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    Intent intentCensor = new Intent(getActivity(), CensorActivity.class);

                                    ArrayList<PlayerStatistic> players = new ArrayList<>();
                                    JsonArray applicants = response.body().getAsJsonObject().get("result").getAsJsonArray();
                                    if (applicants.size() > 0) {
                                        for (JsonElement e : applicants) {
                                            JsonObject app = e.getAsJsonObject();
                                            JsonObject js = app.get("player").getAsJsonObject();
                                            Player applicant =
                                                    new Player(js.get("pid").getAsInt(), js.get("pbirth").getAsInt(),
                                                            js.get("pname").getAsString(), js.get("ppos").getAsString(),
                                                            js.get("picon").getAsString(), js.get("pphone").getAsString(),
                                                            false, new Account(), new Team());

                                            PlayerStatistic application =
                                                    new PlayerStatistic(app.get("id").getAsInt(), applicant,
                                                            app.get("assist").getAsInt(), app.get("block").getAsInt(),
                                                            app.get("goals").getAsInt(), app.get("penalty").getAsInt(),
                                                            app.get("save").getAsInt(), app.get("tackle").getAsInt());
                                            application.setPlayer(applicant);
                                            players.add(application);
                                        }

                                    }
                                    intentCensor.putExtra("teamid", myTeam.getId());
                                    intentCensor.putExtra("applications", players);
                                    intentCensor.putExtra("teamname", myTeam.getName());
                                    startActivityForResult(intentCensor, 1);
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            if (res == TEAM_MEMBER) {
                root = inflater.inflate(R.layout.fragment_team, container, false);
                Log.d("my team size", playersFW.size() + "");
                initViewFW(root);
                initViewMF(root);
                initViewDF(root);
                initViewGK(root);
            }

        }

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1 && resultCode == 2){
            getFragmentManager()
                    .beginTransaction()
                    .detach(mtf)
                    .attach(mtf)
                    .commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void initViewFW(View root) {
        if (pl.isCaptain()) {
            viewFW = root.findViewById(R.id.list_fw_cap);
        } else viewFW = root.findViewById(R.id.list_fw);
        adapterFW = new RecyclerViewPlayerAdapter(playersFW, getContext());
        viewFW.setAdapter(adapterFW);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        viewFW.setLayoutManager(linearLayoutManager);
    }

    public void initViewMF(View root) {
        if (pl.isCaptain()) {
            viewMF = root.findViewById(R.id.list_mf_cap);
        } else viewMF = root.findViewById(R.id.list_mf);
        adapterMF = new RecyclerViewPlayerAdapter(playersMF, getContext());
        viewMF.setAdapter(adapterMF);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        viewMF.setLayoutManager(linearLayoutManager);
    }

    public void initViewDF(View root) {
        if (pl.isCaptain()) {
            viewDF = root.findViewById(R.id.list_df_cap);
        } else viewDF = root.findViewById(R.id.list_df);
        adapterDF = new RecyclerViewPlayerAdapter(playersDF, getContext());
        viewDF.setAdapter(adapterDF);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        viewDF.setLayoutManager(linearLayoutManager);
    }

    public void initViewGK(View root) {
        if (pl.isCaptain()) {
            viewGK = root.findViewById(R.id.list_gk_cap);
        } else viewGK = root.findViewById(R.id.list_gk);
        adapterGK = new RecyclerViewPlayerAdapter(playersGK, getContext());
        viewGK.setAdapter(adapterGK);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        viewGK.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataLoader = (OnDataLoad) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myTeamViewModel =
                new ViewModelProvider(MyTeamFragment.this).get(MyTeamViewModel.class);

        mapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);
        uiUpdate = new MyTeamFrgmUiUpdate();
        getActivity().registerReceiver(uiUpdate, new IntentFilter("UPDATE_CEN_NUM"));

    }

    @Override
    public void onStop() {
        super.onStop();
//        getActivity().unregisterReceiver(uiUpdate);
    }


    class PlayerInfoTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            int res = 0;
            try {

                if (!taskRunning) {
                    Log.d("task is running", "true");
                    JSONObject data = new JSONObject();
                    data.put("id", user.getId());
                    MyService myRetrofit = RetrofitClient.getInstance(baseURL).create(MyService.class);
                    Call<JsonObject> callBack = myRetrofit.getPlayer(data);

                    Response<JsonObject> response = callBack.execute();
                    JsonObject rspObj = response.body();
                    int idPlayer = rspObj.get("id").getAsInt();
                    JsonObject team = rspObj.get("teamid").getAsJsonObject();
                    String name = rspObj.get("name").getAsString();
                    int birt = rspObj.get("birthyear").getAsInt();
                    String position = rspObj.get("position").getAsString();
                    boolean isCaptain = rspObj.get("iscaptain").getAsBoolean();

                    String icon = "";
                    String phone = rspObj.get("phone").getAsString();
                    if (team.size() > 1) {
                        Log.d("hello", "ok");
                        myTeam.setId(team.get("tid").getAsInt());
                        myTeam.setName(team.get("tname").getAsString());
                        myTeam.setRating(team.get("trating").getAsDouble());
                        myTeam.setPhone(team.get("tphone").getAsString());
                        myTeam.setIcon(team.get("ticon").getAsString());
                        myTeam.setWin(team.get("twin").getAsInt());
                        myTeam.setDraw(team.get("tdraw").getAsInt());
                        myTeam.setLose(team.get("tlose").getAsInt());
                        TeamLocation tl = new TeamLocation(team.get("tlid").getAsInt(),
                                team.get("street").getAsString(), team.get("district").getAsString(),
                                team.get("city").getAsString(), team.get("lat").getAsInt(),
                                team.get("lon").getAsInt());

                        myTeam.settLocation(tl);
                        pl.setTeam(myTeam);
                        JsonArray teamPlayer = rspObj.get("teamplayers").getAsJsonArray();
                        Log.d("team", teamPlayer.toString());
                        for (JsonElement o : teamPlayer) {
//                            Log.d("team player day", o.getAsJsonObject().get("name").getAsString());
                            Player fet = new Player(o.getAsJsonObject().get("id").getAsInt(),
                                    o.getAsJsonObject().get("birthyear").getAsInt(),
                                    o.getAsJsonObject().get("name").getAsString(),
                                    o.getAsJsonObject().get("position").getAsString(),
                                    o.getAsJsonObject().get("icon").getAsString(),
                                    o.getAsJsonObject().get("phone").getAsString(),
                                    o.getAsJsonObject().get("isCaptain").getAsBoolean(),
                                    null, myTeam);
                            myTeam.getPlayers().add(fet);
                            switch (o.getAsJsonObject().get("position").getAsString()) {
                                case "FW":
                                    playersFW.add(fet);
                                    break;
                                case "MF":
                                    playersMF.add(fet);
                                    break;
                                case "DF":
                                    playersDF.add(fet);
                                    break;
                                case "GK":
                                    playersGK.add(fet);
                                    break;
                            }
                        }
                        mapViewModel.setmTeam(myTeam);

                    } else {
                        JsonArray rspArr = rspObj.get("teams").getAsJsonArray();
                        for (JsonElement j : rspArr) {
                            teams.add(new Team(j.getAsJsonObject().get("id").getAsInt(),
                                    j.getAsJsonObject().get("name").getAsString(),
                                    j.getAsJsonObject().get("icon").getAsString(),
                                    j.getAsJsonObject().get("rating").getAsDouble(),
                                    new ArrayList<>(),
                                    j.getAsJsonObject().get("win").getAsInt(),
                                    j.getAsJsonObject().get("draw").getAsInt(),
                                    j.getAsJsonObject().get("lose").getAsInt(),
                                    false,
                                    new TeamLocation(j.getAsJsonObject().get("teamlocationid").getAsInt(),
                                            j.getAsJsonObject().get("street").getAsString(),
                                            j.getAsJsonObject().get("district").getAsString(),
                                            j.getAsJsonObject().get("city").getAsString(),
                                            j.getAsJsonObject().get("latitude").getAsDouble(),
                                            j.getAsJsonObject().get("longtitude").getAsDouble())));
                        }

                        JsonArray apply = rspObj.get("apply").getAsJsonArray();
                        Log.d("apply", apply.toString());
                        for (JsonElement e : apply) {
                            arrApply.add(e.getAsJsonObject().get("teamid").getAsInt());
                        }
                    }
                    pl.setId(idPlayer);
                    pl.setName(name);
                    pl.setBirthYear(birt);
                    pl.setPosition(position);
                    pl.setCaptain(isCaptain);
                    pl.setIcon(icon);
                    pl.setPhone(phone);
                    dataLoader.loadPlayerData(pl, myTeam);

                    mapViewModel.setmPlayer(pl);

                }
                if (pl.getTeam().getName().isEmpty()) {
                    res = NO_TEAM;
                } else if (!pl.getTeam().getName().isEmpty() && pl.isCaptain()) {
                    res = TEAM_CAPTAIN;
                } else if (!pl.getTeam().getName().isEmpty() && !pl.isCaptain()) {

                    res = TEAM_MEMBER;
                }
            } catch (JSONException | UnsupportedOperationException | IOException e) {
                e.printStackTrace();
            }
            return res;
        }
    }
}