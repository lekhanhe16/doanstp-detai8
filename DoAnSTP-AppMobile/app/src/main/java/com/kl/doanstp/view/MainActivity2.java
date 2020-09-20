package com.kl.doanstp.view;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.kl.doanstp.R;
import com.kl.doanstp.SessionManager;
import com.kl.doanstp.model.Account;
import com.kl.doanstp.model.Player;
import com.kl.doanstp.model.Team;
import com.kl.doanstp.service.ConnectionUtil;
import com.kl.doanstp.service.MyService;
import com.kl.doanstp.service.RetrofitClient;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends AppCompatActivity implements MyTeamFragment.OnDataLoad {
    public static String CHANNEL_NEW_MATCH = "new match";
    public static String CHANNEL_APPLY = "player apply";
    public static String channelName = "Khanh le dep trai";

    private AppBarConfiguration mAppBarConfiguration;
    TextView userName;
    ImageView avatar;
    private Account user;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    Player thisPlayer = new Player();
    Team myTeam;
    private boolean buttonNoti = false;
    TextView txtBtnNoti;
    String token = FirebaseInstanceId.getInstance().getToken();

    ButtonNotiUiUpdate uiUpdate;
    class ButtonNotiUiUpdate extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("UPDATE_FLOAT_BUTTON_NOTI")){
                buttonNoti = true;
                txtBtnNoti.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        unregisterReceiver(uiUpdate);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(uiUpdate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        uiUpdate = new ButtonNotiUiUpdate();
        registerReceiver(uiUpdate, new IntentFilter("UPDATE_FLOAT_BUTTON_NOTI"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notiChannelNewMatch =
                    new NotificationChannel(CHANNEL_NEW_MATCH, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationChannel notiChannelApply =
                    new NotificationChannel(CHANNEL_APPLY, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notiChannelNewMatch);
            manager.createNotificationChannel(notiChannelApply);

        }
        SessionManager.getInstance().setContext(this);
        SessionManager.getInstance().checkLogin();
        user = SessionManager.getInstance().getUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        txtBtnNoti = findViewById(R.id.button_noti);
        if (buttonNoti){
            txtBtnNoti.setVisibility(View.VISIBLE);
        }
        else {
            txtBtnNoti.setVisibility(View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonNoti = false;
                txtBtnNoti.setVisibility(View.GONE);
                Intent intent = new Intent(MainActivity2.this, MatchControlActivity.class);
                intent.putExtra("player", thisPlayer);
                intent.putExtra("team", myTeam);

                startActivity(intent);

            }
        });
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_team_manage, R.id.nav_map, R.id.nav_slideshow, R.id.nav_log_out)
                .setDrawerLayout(drawer)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(item -> {
            if (R.id.nav_log_out == item.getItemId()) {
                SessionManager.getInstance().logOut();
                Toast.makeText(getBaseContext(), "Logged out", Toast.LENGTH_LONG).show();
            }
            boolean b = NavigationUI.onNavDestinationSelected(item, navController);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
        userName = navigationView.getHeaderView(0).findViewById(R.id.user_name);


        avatar = navigationView.getHeaderView(0).findViewById(R.id.imageView);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void loadPlayerData(Player player, Team team) {
        thisPlayer = player;
        myTeam = team;
        if (thisPlayer.getName() != null) {
            userName.setText(thisPlayer.getName());
        }
        JSONObject data = new JSONObject();
        try {
            data.put("regid", token);
            data.put("playerid", thisPlayer.getId());
            MyService myRetrofit = RetrofitClient.getInstance(ConnectionUtil.baseURL).create(MyService.class);
            Call<JsonObject> callBack = myRetrofit.updateRegID(data);
            callBack.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NotNull Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        Log.d("token", "OK");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}