package com.kl.doanstp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.kl.doanstp.R;
import com.kl.doanstp.adapter.MatchControlDataLoad;
import com.kl.doanstp.model.Player;
import com.kl.doanstp.model.Team;
import com.kl.doanstp.viewmodel.host.HostViewModel;
import com.kl.doanstp.viewmodel.map.MapViewModel;

public class MatchControlActivity extends AppCompatActivity implements MatchControlDataLoad {
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    Player p;
    Team t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        hostViewModel = new ViewModelProvider(this).get(HostViewModel.class);
        setContentView(R.layout.activity_notification_control);
        Intent intent = getIntent();
        p = (Player) intent.getSerializableExtra("player");
        t = (Team) intent.getSerializableExtra("team");
//        Log.d("teamplayername", p.getName()+" "+t.getName());
//        hostViewModel.sethPlayer(p);
//        hostViewModel.sethTeam(t);

        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new com.kl.doanstp.adapter.PagerAdapter(getSupportFragmentManager(), 2);
        viewPager.setAdapter(pagerAdapter);


    }

    @Override
    public Team onGetTeamData() {
        return t;
    }

    @Override
    public Player onGetPlayerData() {
        return p;
    }
}