package com.kl.doanstp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kl.doanstp.R;
import com.kl.doanstp.model.Player;
import com.kl.doanstp.model.Team;
import com.kl.doanstp.service.ConnectionUtil;
import com.kl.doanstp.service.MyService;
import com.kl.doanstp.service.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewTeamAdapter extends RecyclerView.Adapter<RecyclerViewTeamAdapter.RecyclerViewHolder> {

    RecyclerViewTeamAdapter.RecyclerViewHolder viewHolder;
    ArrayList<Team> teams;
    Context context;
    Team t;
    private ArrayList<Player> playersFW = new ArrayList<>();
    private ArrayList<Player> playersMF = new ArrayList<>();
    private ArrayList<Player> playersDF = new ArrayList<>();
    private ArrayList<Player> playersGK = new ArrayList<>();
    private ArrayList<Integer> applicattions;
    private static String baseURL = ConnectionUtil.baseURL;
    private RecyclerView viewFW, viewMF, viewDF, viewGK;
    private RecyclerViewPlayerAdapter adapterFW, adapterMF, adapterDF, adapterGK;
    private Player thisPlayer;

    public RecyclerViewTeamAdapter(ArrayList<Team> arrTeams, Player p, Context context, ArrayList<Integer> a) {
        this.teams = arrTeams;
        this.thisPlayer = p;
        this.context = context;
        this.applicattions = a;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.cardview_apply, parent, false);
        viewHolder = new RecyclerViewHolder(view);
        return viewHolder;
    }

    public RecyclerViewHolder getViewHolder() {
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.teamName.setText(teams.get(position).getName());
        holder.expandableLayout.setVisibility(teams.get(position).isExpanded() ? View.VISIBLE : View.GONE);

        holder.teamRating.setText(teams.get(position).getRating() + "");
        if (applicattions.contains(teams.get(position).getId())) {
            holder.btnApply.setText("HUY DANG KY");
        } else {
            holder.btnApply.setText("DANG KY");
        }
    }

    public void initViewFW(View root) {
        viewFW = root.findViewById(R.id.pop_list_fw);
        adapterFW = new RecyclerViewPlayerAdapter(playersFW, context);
        viewFW.setAdapter(adapterFW);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        viewFW.setLayoutManager(linearLayoutManager);
    }

    public void initViewMF(View root) {
        viewMF = root.findViewById(R.id.pop_list_mf);
        adapterMF = new RecyclerViewPlayerAdapter(playersMF, context);
        viewMF.setAdapter(adapterMF);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        viewMF.setLayoutManager(linearLayoutManager);
    }

    public void initViewDF(View root) {
        viewDF = root.findViewById(R.id.pop_list_df);
        adapterDF = new RecyclerViewPlayerAdapter(playersDF, context);
        viewDF.setAdapter(adapterDF);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        viewDF.setLayoutManager(linearLayoutManager);
    }

    public void initViewGK(View root) {
        viewGK = root.findViewById(R.id.pop_list_gk);
        adapterGK = new RecyclerViewPlayerAdapter(playersGK, context);
        viewGK.setAdapter(adapterGK);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        viewGK.setLayoutManager(linearLayoutManager);

    }

    @Override
    public int getItemCount() {
        return this.teams.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView teamName, teamRating;
        Button btnDetail;
        Button btnApply;
        ConstraintLayout expandableLayout;
        RelativeLayout rlt;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            teamName = itemView.findViewById(R.id.txt_apply_teamname);
            teamRating = itemView.findViewById(R.id.txt_apply_rating);


            rlt = itemView.findViewById(R.id.rlt_layout);
            expandableLayout = itemView.findViewById(R.id.constrain_cv_team);
            btnApply = expandableLayout.findViewById(R.id.btn_apply);
            btnDetail = expandableLayout.findViewById(R.id.btn_detail);
            rlt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    t = teams.get(getAdapterPosition());
                    t.setExpanded(!t.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (teams.get(getAdapterPosition()).getPlayers().size() == 0) {

                        MyService myRetrofit = RetrofitClient.getInstance(baseURL).create(MyService.class);
                        playersDF.clear();
                        playersFW.clear();
                        playersGK.clear();
                        playersMF.clear();
                        try {
                            JSONObject data = new JSONObject();
                            data.put("teamid", teams.get(getAdapterPosition()).getId());
                            Call<JsonObject> callBack = myRetrofit.getTeamPlayers(data);
                            callBack.enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    JsonArray arrPlayers = response.body().get("teamplayers").getAsJsonArray();
                                    for (JsonElement p : arrPlayers) {
                                        Player fet = new Player(
                                                p.getAsJsonObject().get("id").getAsInt(),
                                                p.getAsJsonObject().get("birthyear").getAsInt(),
                                                p.getAsJsonObject().get("name").getAsString(),
                                                p.getAsJsonObject().get("position").getAsString(),
                                                p.getAsJsonObject().get("icon").getAsString(),
                                                p.getAsJsonObject().get("phone").getAsString(),
                                                p.getAsJsonObject().get("isCaptain").getAsBoolean(),
                                                null, teams.get(getAdapterPosition()));
                                        teams.get(getAdapterPosition()).getPlayers().add(fet);
                                        switch (fet.getPosition()) {
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

                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    LayoutInflater inflater = (LayoutInflater)
                            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popup_view_team, null);
                    // create the popup window
                    int width = LinearLayout.LayoutParams.MATCH_PARENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                    popupWindow.showAtLocation(v, Gravity.CENTER_VERTICAL, 0, 0);
                    ImageView teamIcon = popupView.findViewById(R.id.pop_team_icon);
                    TextView teamName = popupView.findViewById(R.id.pop_team_name);
                    TextView teamWDL = popupView.findViewById(R.id.pop_team_wdl);
                    Team team = teams.get(getAdapterPosition());
                    teamName.setText(team.getName());

                    initViewFW(popupView);
                    initViewMF(popupView);
                    initViewDF(popupView);
                    initViewGK(popupView);

                }
            });
            btnApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (btnApply.getText().toString().equalsIgnoreCase("DANG KY")) {
                        JSONObject data = new JSONObject();
                        try {
                            data.put("playerid", thisPlayer.getId());
                            data.put("teamid", teams.get(getAdapterPosition()).getId());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        MyService myRetrofit = RetrofitClient.getInstance(baseURL).create(MyService.class);
                        Call<JsonObject> callBack = myRetrofit.applyToTeam(data);
                        callBack.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                btnApply.setText("HUY DANG KY");
                                applicattions.add(teams.get(getAdapterPosition()).getId());
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {

                            }
                        });
                    } else if (btnApply.getText().toString().equalsIgnoreCase("HUY DANG KY")) {
                        JSONObject data = new JSONObject();
                        try {
                            data.put("playerid", thisPlayer.getId());
                            data.put("teamid", teams.get(getAdapterPosition()).getId());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        MyService myRetrofit = RetrofitClient.getInstance(baseURL).create(MyService.class);
                        Call<JsonObject> callBack = myRetrofit.cancelApply(data);
                        callBack.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                btnApply.setText("DANG KY");
                                Log.d("position", getAdapterPosition() + "");
                                applicattions.remove((Integer) teams.get(getAdapterPosition()).getId());
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {

                            }
                        });

                    }
                }
            });
        }
    }

    public int getCaptain(int position) {
        int captainID = 0;
        for (Player p : teams.get(position).getPlayers()) {
            if (p.isCaptain()) {
                captainID = p.getId();
                break;
            }
        }
        return captainID;
    }
}
