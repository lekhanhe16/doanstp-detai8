package com.kl.doanstp.adapter;

import android.content.Context;
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
import com.kl.doanstp.model.DraftMatchTeam;

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

public class ExpandLayoutAdapter extends RecyclerView.Adapter<ExpandLayoutAdapter.ExViewHolder> {
    public Context context;
    public ArrayList<DraftMatchTeam> dmt;
    private boolean isClick;
    private static String baseURL = ConnectionUtil.baseURL;
    private int adaptPosition;
    private Player thisPlayer;
    public ExpandLayoutAdapter(Context context, ArrayList<DraftMatchTeam> dmTem, boolean isClick, Player player) {
        this.context = context;
        this.dmt = dmTem;
        this.isClick = isClick;
        this.thisPlayer = player;
    }

    @NonNull
    @Override
    public ExpandLayoutAdapter.ExViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.item_oppenent, parent, false);
        return new ExViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpandLayoutAdapter.ExViewHolder holder, int position) {
        Team team = dmt.get(position).getTeam();
        holder.foeName.setText(team.getName() + " muon tham gia");
        holder.foeRating.setText(team.getRating() + "");
        if (thisPlayer.isCaptain()){
            if (dmt.get(position).isReady()) {
                holder.btnAccept.setText("Bo");
            } else if (!dmt.get(position).isReady() && isClick) {
                holder.btnAccept.setEnabled(false);

            }
            else if(!dmt.get(position).isReady() && !isClick){
                holder.btnAccept.setEnabled(true);
                holder.btnAccept.setText("Dong y");
            }
        }

        else {
            holder.btnAccept.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return this.dmt.size();
    }

    public class ExViewHolder extends RecyclerView.ViewHolder {
        TextView foeName, foeRating;
        Button btnAccept;

        public ExViewHolder(@NonNull View itemView) {
            super(itemView);
            foeName = itemView.findViewById(R.id.foe_teamname);
            foeRating = itemView.findViewById(R.id.foe_rating);
            btnAccept = itemView.findViewById(R.id.foe_btnAccept);
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isClick = true;
                    adaptPosition = getAdapterPosition();
                    if (btnAccept.getText().toString().equalsIgnoreCase("Dong y")) {
                        Log.d("adapt pos", getAdapterPosition()+"");
                        for (int i = 0; i < dmt.size(); i++) {
                            if (adaptPosition != i) {
                                dmt.get(i).setReady(false);
                            }
                            if(adaptPosition == i){
                                dmt.get(i).setReady(true);
                            }
                        }
                        notifyDataSetChanged();

                        JSONObject data = new JSONObject();
                        try {
                            data.put("matchid", dmt.get(adaptPosition).getdMatch().getId());
                            data.put("teamid", dmt.get(adaptPosition).getTeam().getId());

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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        for (int i = 0; i < dmt.size(); i++) {
                            dmt.get(i).setReady(false);
                        }
                        isClick = false;
                        notifyDataSetChanged();

                        JSONObject data = new JSONObject();
                        try {
                            data.put("matchid", dmt.get(adaptPosition).getdMatch().getId());
                            data.put("teamid", dmt.get(adaptPosition).getTeam().getId());
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}
