package com.kl.doanstp.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.kl.doanstp.R;
import com.kl.doanstp.model.DraftedMatch;
import com.kl.doanstp.model.Player;
import com.kl.doanstp.model.Team;
import com.kl.doanstp.service.ConnectionUtil;
import com.kl.doanstp.service.MyService;
import com.kl.doanstp.service.RetrofitClient;
import com.kl.doanstp.view.MyTeamFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerAwayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<DraftedMatch> arrDM;
    private Context context;
    private static String baseURL = ConnectionUtil.baseURL;
    private int adaptPosition;
    private Team myTeam;
    private Player thisplayer;
    private final int INVITED_MATCH = 1;
    private final int JOIN_MATCH = 2;

    public RecyclerAwayAdapter(Context context, ArrayList arrDM, Team team, Player player) {
        this.context = context;
        this.arrDM = arrDM;
        this.myTeam = team;
        this.thisplayer = player;
        Log.d("dm size", arrDM.size() + "");
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        if (arrDM.get(position).getDmt().get(0).getRole().equalsIgnoreCase("create")) {
            type = JOIN_MATCH;
        } else if (arrDM.get(position).getDmt().get(0).getRole().equalsIgnoreCase("invite")) {
            type = INVITED_MATCH;
        }
        return type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        if (viewType == INVITED_MATCH) {
            View v = LayoutInflater.from(this.context).inflate(R.layout.invited_match, parent, false);
            vh = new InvitedViewHolder(v);
        } else if (viewType == JOIN_MATCH) {
            View v = LayoutInflater.from(this.context).inflate(R.layout.join_match, parent, false);
            vh = new JoinViewHolder(v);

        }
        return vh;
    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Team t = arrDM.get(position).getDmt().get(0).getTeam();
        if (getItemViewType(position) == INVITED_MATCH) {

            InvitedViewHolder vh = (InvitedViewHolder) holder;
            if (!arrDM.get(position).isEnd()) {
                if (!thisplayer.isCaptain()) {
                    vh.deny.setVisibility(View.GONE);
                    vh.accept.setVisibility(View.GONE);
                }

                if (!arrDM.get(position).getDmt().get(1).isReady()) {
                    if (!arrDM.get(position).isDenied()) {
                        vh.title.setText(t.getName() + " moi doi ban da");
                        vh.accept.setText("Dong y");
                    } else {
                        vh.title.setText(Html.fromHtml(t.getName() + " moi doi ban da "
                                + getColoredSpanned("Tu choi", "#FF0000"), Html.FROM_HTML_MODE_LEGACY));
                        vh.deny.setVisibility(View.GONE);
                        vh.accept.setVisibility(View.GONE);
                    }
                } else {
                    vh.title.setText(t.getName() + " moi doi ban da");
                    vh.accept.setText("Huy");
                }
                if (arrDM.get(position).isAccepted()) {

                    vh.title.setText(Html.fromHtml(t.getName() + " moi doi ban da "
                            + getColoredSpanned("OK", "#00FF00"), Html.FROM_HTML_MODE_LEGACY));
                    vh.accept.setVisibility(View.GONE);
                    vh.deny.setVisibility(View.GONE);
                }


            } else if (arrDM.get(position).isEnd()) {
                // update code here
                if (arrDM.get(position).getDmt().get(0).getResstt() == 1) {
                    //win
                    vh.title.setText(Html.fromHtml(t.getName() + " " + arrDM.get(position).getScoreTable().getFinalScore()
                            + " " + myTeam.getName() + "  "
                            + getColoredSpanned("Thang", "#00FF00"), Html.FROM_HTML_MODE_LEGACY));
                } else if (arrDM.get(position).getDmt().get(0).getResstt() == 2) {
                    //draw
                    vh.title.setText(Html.fromHtml(t.getName() + " " + arrDM.get(position).getScoreTable().getFinalScore()
                            + " " + myTeam.getName() + "  "
                            + getColoredSpanned("Hoa", "#FFFF00"), Html.FROM_HTML_MODE_LEGACY));
                } else if (arrDM.get(position).getDmt().get(0).getResstt() == 3) {
                    // lose
                    vh.title.setText(Html.fromHtml(t.getName() + " " + arrDM.get(position).getScoreTable().getFinalScore()
                            + " " + myTeam.getName() + "  "
                            + getColoredSpanned("Thua", "#FF0000"), Html.FROM_HTML_MODE_LEGACY));
                }

                vh.deny.setVisibility(View.GONE);
                vh.accept.setVisibility(View.GONE);

            }
            vh.timedate.setText(arrDM.get(position).getDay() + " luc " + arrDM.get(position).getTime());
            vh.place.setText(arrDM.get(position).getPlace());
        } else if (getItemViewType(position) == JOIN_MATCH) {
            JoinViewHolder jvh = (JoinViewHolder) holder;
            if (!arrDM.get(position).isEnd()) {
                if (!thisplayer.isCaptain()) {
                    jvh.cancel.setVisibility(View.GONE);
                }
                if (arrDM.get(position).getDmt().get(1).isReady() && arrDM.get(position).isAccepted()) {
                    jvh.cancel.setVisibility(View.GONE);
                    jvh.content.setText(Html.fromHtml("Doi ban tham gia tran dau cua "
                            + arrDM.get(position).getDmt().get(0).getTeam().getName()
                            + ".  " + getColoredSpanned("OK", "#00FF00"), Html.FROM_HTML_MODE_LEGACY));
                }
                else if (!arrDM.get(position).getDmt().get(1).isReady() && arrDM.get(position).isAccepted()){
                    jvh.cancel.setVisibility(View.GONE);
                    jvh.content.setText(Html.fromHtml("Doi ban tham gia tran dau cua "
                            + arrDM.get(position).getDmt().get(0).getTeam().getName()
                            + ".  " + getColoredSpanned("Bi tu choi", "#FF0000"), Html.FROM_HTML_MODE_LEGACY));
                }
                else {
                    jvh.content.setText("Doi ban tham gia tran dau cua " + arrDM.get(position).
                            getDmt().get(0).getTeam().getName() + ".");
                }
            } else {
                jvh.cancel.setVisibility(View.GONE);
                // win
                if (arrDM.get(position).getDmt().get(0).getResstt() == 1) {
                    jvh.content.setText(Html.fromHtml(arrDM.get(position).getDmt().get(0).getTeam().getName()
                            + " " + arrDM.get(position).getScoreTable().getFinalScore()
                            + " " + myTeam.getName()
                            + "  " + getColoredSpanned("Thang", "#00FF00"), Html.FROM_HTML_MODE_LEGACY));
                }
                //draw

                else if (arrDM.get(position).getDmt().get(0).getResstt() == 2) {
                    jvh.content.setText(Html.fromHtml( arrDM.get(position).getDmt().get(0).getTeam().getName()
                            + " " + arrDM.get(position).getScoreTable().getFinalScore()
                            + " " + myTeam.getName()
                            + "  " + getColoredSpanned("Hoa", "#FFFF00"), Html.FROM_HTML_MODE_LEGACY));
                }
                //lose
                else if (arrDM.get(position).getDmt().get(0).getResstt() == 3) {
                    jvh.content.setText(Html.fromHtml( arrDM.get(position).getDmt().get(0).getTeam().getName()
                            + " " + arrDM.get(position).getScoreTable().getFinalScore()
                            + " " + myTeam.getName()
                            + "  " + getColoredSpanned("Thua", "#FF0000"), Html.FROM_HTML_MODE_LEGACY));
                }
            }
            jvh.place.setText(arrDM.get(position).getPlace());
            jvh.time.setText(arrDM.get(position).getDay() + " luc " + arrDM.get(position).getTime());
        }
    }

    @Override
    public int getItemCount() {
        return this.arrDM.size();
    }

    public class JoinViewHolder extends RecyclerView.ViewHolder {
        TextView content, time, place;
        Button cancel;

        public JoinViewHolder(@NonNull View itemView) {
            super(itemView);


            content = itemView.findViewById(R.id.join_item_content);
            time = itemView.findViewById(R.id.join_item_thoigian);
            place = itemView.findViewById(R.id.join_item_diadiem);

            cancel = itemView.findViewById(R.id.join_btn_deny);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adaptPosition = getAdapterPosition();
                    JSONObject data = new JSONObject();
                    try {
                        data.put("teamid", myTeam.getId());
                        data.put("matchid", arrDM.get(adaptPosition).getId());
                        data.put("command", 2);
                        notifyItemRemoved(adaptPosition);
                        arrDM.remove(adaptPosition);

                        MyService myRetrofit = RetrofitClient.getInstance(ConnectionUtil.baseURL).create(MyService.class);
                        Call<JsonObject> callBack = myRetrofit.denyMatch(data);
                        callBack.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

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
    }

    public class InvitedViewHolder extends RecyclerView.ViewHolder {
        Button accept, deny;
        TextView title, timedate, place;

        public InvitedViewHolder(@NonNull View itemView) {
            super(itemView);
            accept = itemView.findViewById(R.id.invited_btn_accept);
            deny = itemView.findViewById(R.id.invited_btn_deny);
            title = itemView.findViewById(R.id.invited_item_content);
            timedate = itemView.findViewById(R.id.invited_item_thoigian);
            place = itemView.findViewById(R.id.invited_item_diadiem);
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adaptPosition = getAdapterPosition();
                    JSONObject data = new JSONObject();
                    try {
                        data.put("teamid", myTeam.getId());
                        data.put("matchid", arrDM.get(adaptPosition).getId());
                        if (accept.getText().toString().equalsIgnoreCase("Dong y")) {

                            arrDM.get(adaptPosition).getDmt().get(1).setReady(true);
                            notifyItemChanged(adaptPosition);
                            MyService myRetrofit = RetrofitClient.getInstance(baseURL).create(MyService.class);
                            Call<JsonObject> callBack = myRetrofit.acceptMatch(data);
                            callBack.enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {

                                }
                            });
                        } else {
                            arrDM.get(adaptPosition).getDmt().get(1).setReady(false);
                            notifyItemChanged(adaptPosition);
                            data.put("command", 0);
                            MyService myRetrofit = RetrofitClient.getInstance(baseURL).create(MyService.class);
                            Call<JsonObject> callBack = myRetrofit.denyMatch(data);
                            callBack.enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {

                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
            deny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adaptPosition = getAdapterPosition();
                    JSONObject data = new JSONObject();
                    try {
                        data.put("teamid", arrDM.get(adaptPosition).getDmt().get(1).getTeam().getId());
                        data.put("matchid", arrDM.get(adaptPosition).getId());
                        data.put("command", 1);
//                        arrDM.remove(adaptPosition);
//                        notifyItemRemoved(adaptPosition);
                        arrDM.get(adaptPosition).getDmt().get(1).setReady(false);
                        arrDM.get(adaptPosition).setDenied(true);
                        notifyItemChanged(adaptPosition);
                        MyService myRetrofit = RetrofitClient.getInstance(baseURL).create(MyService.class);
                        Call<JsonObject> callBack = myRetrofit.denyMatch(data);
                        callBack.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

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
    }

}
