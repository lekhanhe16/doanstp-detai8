package com.kl.doanstp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.kl.doanstp.R;
import com.kl.doanstp.model.DraftedMatch;
import com.kl.doanstp.service.ConnectionUtil;
import com.kl.doanstp.service.MyService;
import com.kl.doanstp.service.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ArrayListMatchAdapter extends ArrayAdapter<DraftedMatch> {
    private Context context;
    private ArrayList<DraftedMatch> matches;
    private int resID, teamID;
    private static String baseURL = ConnectionUtil.baseURL;

    public ArrayListMatchAdapter(@NonNull Context context, int resource, @NonNull ArrayList objects,
                                 int teamID) {
        super(context, resource, objects);
        this.context = context;
        this.resID = resource;
        this.matches = objects;
        this.teamID = teamID;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(this.context).inflate(this.resID, parent, false);
            TextView matchid, matchday, matchplace;
            Button btnJoin, btnChiDuong;

            matchid = v.findViewById(R.id.matchid);
            matchday = v.findViewById(R.id.matchday);
            matchplace = v.findViewById(R.id.matchplace);

            btnChiDuong = v.findViewById(R.id.join_btn_chiduong);
            btnJoin = v.findViewById(R.id.join_btn_join);

            matchid.setText("Ma tran: " + matches.get(position).getId());
            matchday.setText("Ngay: " + matches.get(position).getDay().toString());
            matchplace.setText("Dia diem: " + matches.get(position).getPlace());

            btnJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        JSONObject data = new JSONObject();
                        data.put("teamid", teamID);
                        data.put("matchid", matches.get(position).getId());

                        MyService myRetrofit = RetrofitClient.getInstance(baseURL).create(MyService.class);
                        Call<JsonObject> callBack = myRetrofit.joinMatch(data);
                        callBack.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.isSuccessful()){
                                    ((Activity)context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            matches.remove(position);
                                            notifyDataSetChanged();
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

                }
            });
        }
        return v;
    }

    @Override
    public int getCount() {
        return this.matches.size();
    }
}
