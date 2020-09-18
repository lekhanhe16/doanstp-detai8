package com.kl.doanstp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.kl.doanstp.R;
import com.kl.doanstp.adapter.RecyclerViewCensorAdapter;
import com.kl.doanstp.model.PlayerStatistic;

import java.util.ArrayList;

public class CensorActivity extends AppCompatActivity {
    private ArrayList<PlayerStatistic> applications = new ArrayList<>();
    private RecyclerView listApplications;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_censor);
        Intent intent = getIntent();
        int teamid = intent.getIntExtra("teamid",0);
        String teamname = intent.getStringExtra("teamname");
        applications = (ArrayList<PlayerStatistic>) intent.getSerializableExtra("applications");
        listApplications = findViewById(R.id.list_uv);

        RecyclerViewCensorAdapter adapter = new RecyclerViewCensorAdapter(applications, getBaseContext(),
                teamid, teamname);
        listApplications.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(),
                LinearLayoutManager.VERTICAL, false);
        listApplications.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(2);
        finishActivity(1);
    }
}