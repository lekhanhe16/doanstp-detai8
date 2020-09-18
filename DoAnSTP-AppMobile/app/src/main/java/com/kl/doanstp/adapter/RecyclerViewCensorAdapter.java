package com.kl.doanstp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.kl.doanstp.R;
import com.kl.doanstp.model.Player;
import com.kl.doanstp.model.PlayerStatistic;
import com.kl.doanstp.service.ConnectionUtil;
import com.kl.doanstp.service.MyService;
import com.kl.doanstp.service.RetrofitClient;
import com.kl.doanstp.view.CensorActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewCensorAdapter extends RecyclerView.Adapter<RecyclerViewCensorAdapter.MyViewHolder>{
    private ArrayList<PlayerStatistic> arrPlayer;
    private Context context;
    private static String baseURL = ConnectionUtil.baseURL;
    private int teamid;
    private String teamName;
    public RecyclerViewCensorAdapter (ArrayList<PlayerStatistic> a, Context c, int temid, String name){
        this.arrPlayer = a;
        this.context = c;
        this.teamid = temid;
        this.teamName = name;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.cardview_ungvien, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PlayerStatistic ps = arrPlayer.get(position);
        holder.playerName.setText(ps.getPlayer().getName());
        if(!ps.getPlayer().getIcon().isEmpty()){

        }
        else{
            holder.icon.setImageResource(R.drawable.ball);
        }
        holder.lnl.setVisibility(arrPlayer.get(position).isExpanded() ? View.VISIBLE : View.GONE);
        holder.playerBirth.setText("Nam sinh: "+ps.getPlayer().getBirthYear()+"");
        holder.playerSDT.setText("SDT: "+ps.getPlayer().getPhone());
        holder.playerPosition.setText("Vi tri: "+ps.getPlayer().getPosition());
        holder.playerStat.setText("Ban thang: "+ps.getGoals()+", Tac bong: "+ps.getTackle()+
                ", Can pha: "+ps.getBlock()+", Kien tao: "+ps.getAssist()+", Cuu thua: "+ps.getSave());
    }

    @Override
    public int getItemCount() {
        return this.arrPlayer.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView playerName, playerBirth, playerSDT, playerPosition, playerStat;
        ImageView icon;
        Button accept, reject;
        RelativeLayout rlt;
        LinearLayout lnl;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.uv_name);
            playerBirth = itemView.findViewById(R.id.uv_birthyear);
            playerSDT = itemView.findViewById(R.id.uv_sdt);
            playerPosition = itemView.findViewById(R.id.uv_position);
            playerStat = itemView.findViewById(R.id.thanhtich);
            icon = itemView.findViewById(R.id.uv_icon);
            accept = itemView.findViewById(R.id.accept_uv);
            reject = itemView.findViewById(R.id.reject_uv);

            rlt = itemView.findViewById(R.id.rlt_uv);
            lnl = itemView.findViewById(R.id.info_uv);

            rlt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrPlayer.get(getAdapterPosition()).setExpanded(!arrPlayer.get(getAdapterPosition()).isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        JSONObject data = new JSONObject();
                        data.put("playerid", arrPlayer.get(getAdapterPosition()).getPlayer().getId());
                        data.put("teamid", teamid);
                        data.put("teamname", teamName);
                        MyService myRetrofit = RetrofitClient.getInstance(baseURL).create(MyService.class);
                        Call<JsonObject> callBack = myRetrofit.acceptApplicant(data);
                        callBack.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                arrPlayer.remove(getAdapterPosition());

                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    reject.setEnabled(false);
                    accept.setEnabled(false);
//                    notifyDataSetChanged();
                }
            });
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    try {
                        JSONObject data = new JSONObject();
                        data.put("playerid", arrPlayer.get(pos).getPlayer().getId());
                        data.put("teamid", teamid);
                        MyService myRetrofit = RetrofitClient.getInstance(baseURL).create(MyService.class);
                        Call<JsonObject> callBack = myRetrofit.rejectApplicant(data);
                        callBack.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                arrPlayer.remove(getAdapterPosition());
//                                reject.setEnabled(false);
//                                accept.setEnabled(false);
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    reject.setEnabled(false);
                    accept.setEnabled(false);
                    notifyItemChanged(pos);
//                    notifyDataSetChanged();
                }
            });
        }
    }
}
