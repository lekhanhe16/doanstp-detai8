package com.kl.doanstp.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kl.doanstp.R;

import com.kl.doanstp.adapter.ArrayListMatchAdapter;
import com.kl.doanstp.adapter.InfoWindowMap;
import com.kl.doanstp.adapter.RecyclerViewPlayerAdapter;
import com.kl.doanstp.model.CityDistrict;
import com.kl.doanstp.model.DraftMatchTeam;
import com.kl.doanstp.model.DraftedMatch;
import com.kl.doanstp.model.Player;
import com.kl.doanstp.model.Team;
import com.kl.doanstp.model.TeamLocation;
import com.kl.doanstp.service.ConnectionUtil;
import com.kl.doanstp.service.MyService;
import com.kl.doanstp.service.RetrofitClient;
import com.kl.doanstp.viewmodel.map.MapViewModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapFragment extends Fragment {
    private static String baseURL = ConnectionUtil.baseURL;
    private ArrayList<Player> playersFW = new ArrayList<>();
    private ArrayList<Player> playersMF = new ArrayList<>();
    private ArrayList<Player> playersDF = new ArrayList<>();
    private ArrayList<Player> playersGK = new ArrayList<>();
    private static final int LOCATION_REQUEST = 500;
    private RecyclerView viewFW, viewMF, viewDF, viewGK;
    private RecyclerViewPlayerAdapter adapterFW, adapterMF, adapterDF, adapterGK;
    private Team team, myTeam;
    private Player thisPlayer;
    private MapViewModel mapViewModel;

    private GoogleMap ggMap;
    public SupportMapFragment mapFragment;
    private RelativeLayout rltLayout;
    private TextView teamName, teamPhone, teamRate;
    private ImageButton btnCall;
    private Button btnDoihinh, btnMatch, btnMoi;
    private ArrayList<Team> arrTeams = new ArrayList<>();
    private ArrayList<DraftedMatch> arrDM = new ArrayList<>();
    private ArrayList<Marker> markers = new ArrayList<>();
    int callerId = -1;
    TextView datePicker;
    CityDistrict cd = new CityDistrict();
    public final String DATE_FORMAT = "yyyy-MM-dd";
    private DateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_map, container, false);

        rltLayout = root.findViewById(R.id.info_window);
        rltLayout.setVisibility(View.GONE);

        teamName = root.findViewById(R.id.team_name_rlt);
        teamPhone = root.findViewById(R.id.teamphone_rlt);
        teamRate = root.findViewById(R.id.rate_rlt);

        btnCall = root.findViewById(R.id.btn_call_rlt);
        btnDoihinh = root.findViewById(R.id.btn_doihinh_rlt);
        btnMatch = root.findViewById(R.id.btn_match_rlt);
        btnMoi = root.findViewById(R.id.btn_moi_rlt);

        btnMoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thisPlayer.isCaptain()) {

                    rltLayout.setVisibility(View.GONE);
                    LayoutInflater inflater = (LayoutInflater)
                            getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popup_create_new_match, null);
                    // create the popup window
                    int width = LinearLayout.LayoutParams.MATCH_PARENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                    popupWindow.showAtLocation(getParentFragment().getView(), Gravity.CENTER_VERTICAL, 0, 0);
                    TextView txttimePicker = popupView.findViewById(R.id.txt_create_hour);
                    TextView title = popupView.findViewById(R.id.create_match_title);
                    title.setText("Moi " + team.getName());
                    EditText place = popupView.findViewById(R.id.edit_create_place);
                    Button btnCreateMatch = popupView.findViewById(R.id.btn_create_done);
                    btnCreateMatch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONObject data = new JSONObject();
                            try {
                                data.put("invite", myTeam.getId());
                                data.put("invited", team.getId());
                                data.put("day", datePicker.getText());
                                data.put("time", txttimePicker.getText());
                                data.put("place", place.getText().toString());
                                MyService myRetrofit = RetrofitClient.getInstance(baseURL).create(MyService.class);
                                Call<JsonObject> callBack = myRetrofit.invite(data);
                                callBack.enqueue(new Callback<JsonObject>() {
                                    @Override
                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                        if (response.isSuccessful()) {
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getContext(), response.body().getAsJsonObject()
                                                            .get("result").getAsString(), Toast.LENGTH_LONG).show();
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
                            popupWindow.dismiss();
                        }
                    });
                    txttimePicker.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Calendar mcurrentTime = Calendar.getInstance();
                            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                            int minute = mcurrentTime.get(Calendar.MINUTE);
                            TimePickerDialog mTimePicker;
                            mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                    String hour = selectedHour + "", minute = selectedMinute + "";
                                    if (selectedHour < 10) {
                                        hour = "0" + hour;
                                    }
                                    if (selectedMinute < 10) {
                                        minute = "0" + minute;
                                    }
                                    txttimePicker.setText(hour + ":" + minute);

                                }
                            }, hour, minute, true);//Yes 24 hour time
                            mTimePicker.setTitle("Chon thoi gian thi dau");
                            mTimePicker.show();

                        }
                    });
                    datePicker = popupView.findViewById(R.id.txt_create_day);
                    datePicker.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDatePickerDialog(v.getId(), datePicker.getText().toString().trim());
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Chi doi truong moi co the moi", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rltLayout.setVisibility(View.GONE);
                ArrayList<DraftedMatch> matches = new ArrayList<>();
                for (DraftedMatch match : arrDM) {
                    int kt = 1;
                    if (match.getDmt().get(0).getTeam().getId() == team.getId()) {
                        for (int i = 1; i < match.getDmt().size(); i++) {
                            if (match.getDmt().get(i).getTeam().getId() == myTeam.getId()) {
                                kt = 0;
                                break;
                            }
                        }
                        if (kt == 1) {
                            matches.add(match);
                        }
                    }
                }

                Log.d("num of match: ", matches.size() + "");
                LayoutInflater inflater = (LayoutInflater)
                        getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_join_match, null);
                // create the popup window
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.showAtLocation(v, Gravity.CENTER_VERTICAL, 0, 0);

                ListView lv = popupView.findViewById(R.id.join_list_match);
                ArrayListMatchAdapter adapter = new ArrayListMatchAdapter(getContext(), R.layout.cardview_join_match,
                        matches, myTeam.getId());
                lv.setAdapter(adapter);
            }
        });

        btnDoihinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyService myRetrofit = RetrofitClient.getInstance(baseURL).create(MyService.class);
                playersDF.clear();
                playersFW.clear();
                playersGK.clear();
                playersMF.clear();
                try {
                    JSONObject data = new JSONObject();
                    data.put("teamid", team.getId());
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
                                        null, team);
                                team.getPlayers().add(fet);
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
                LayoutInflater inflater = (LayoutInflater)
                        getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                teamName.setText(team.getName());

                initViewFW(popupView);
                initViewMF(popupView);
                initViewDF(popupView);
                initViewGK(popupView);
            }

        });

        mapViewModel.getPlayer().observe(requireActivity(), new Observer<Player>() {
            @Override
            public void onChanged(Player player) {
                thisPlayer = player;
            }
        });

        mapViewModel.getTeam().observe(requireActivity(), new Observer<Team>() {
            @Override
            public void onChanged(Team team) {
                myTeam = team;

            }
        });
        return root;
    }

    public void initViewFW(View root) {
        viewFW = root.findViewById(R.id.pop_list_fw);
        adapterFW = new RecyclerViewPlayerAdapter(playersFW, getContext());
        viewFW.setAdapter(adapterFW);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        viewFW.setLayoutManager(linearLayoutManager);
    }

    public void initViewMF(View root) {
        viewMF = root.findViewById(R.id.pop_list_mf);
        adapterMF = new RecyclerViewPlayerAdapter(playersMF, getContext());
        viewMF.setAdapter(adapterMF);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        viewMF.setLayoutManager(linearLayoutManager);
    }

    public void initViewDF(View root) {
        viewDF = root.findViewById(R.id.pop_list_df);
        adapterDF = new RecyclerViewPlayerAdapter(playersDF, getContext());
        viewDF.setAdapter(adapterDF);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        viewDF.setLayoutManager(linearLayoutManager);
    }

    public void initViewGK(View root) {
        viewGK = root.findViewById(R.id.pop_list_gk);
        adapterGK = new RecyclerViewPlayerAdapter(playersGK, getContext());
        viewGK.setAdapter(adapterGK);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        viewGK.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST);
                return;
            }
            ggMap = googleMap;
            ggMap.setMyLocationEnabled(true);

            ggMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    for (Marker m : markers) {
                        if (marker.getTitle().equals(m.getTitle())) {
                            rltLayout.setVisibility(View.VISIBLE);
                            teamName.setText(((Team) marker.getTag()).getName());
                            team = (Team) marker.getTag();
                            m.showInfoWindow();
                            break;
                        }
                    }
                    return true;
                }
            });
            ggMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    for (Marker m : markers) {
                        if (marker.getTitle().equals(m.getTitle())) {
                            rltLayout.setVisibility(View.VISIBLE);
                            teamName.setText(((Team) marker.getTag()).getName());
                            team = (Team) marker.getTag();
                        }
                    }

                }
            });
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        mapViewModel =
                new ViewModelProvider(requireActivity()).get(MapViewModel.class);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.match_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_map_create:
                if (thisPlayer.isCaptain()) {

                    rltLayout.setVisibility(View.GONE);
                    LayoutInflater inflater = (LayoutInflater)
                            getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popup_create_new_match, null);
                    // create the popup window
                    int width = LinearLayout.LayoutParams.MATCH_PARENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                    popupWindow.showAtLocation(getParentFragment().getView(), Gravity.CENTER_VERTICAL, 0, 0);
                    TextView txttimePicker = popupView.findViewById(R.id.txt_create_hour);
                    EditText place = popupView.findViewById(R.id.edit_create_place);
                    Button btnCreateMatch = popupView.findViewById(R.id.btn_create_done);
                    btnCreateMatch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONObject data = new JSONObject();
                            try {

                                data.put("teamid", myTeam.getId());
                                data.put("day", datePicker.getText());
                                data.put("place", place.getText().toString());
                                data.put("time", txttimePicker.getText());

                                MyService myRetrofit = RetrofitClient.getInstance(baseURL).create(MyService.class);
                                Call<JsonObject> callBack = myRetrofit.createMatch(data);
                                callBack.enqueue(new Callback<JsonObject>() {
                                    @Override
                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                        if (response.isSuccessful()) {
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getContext(), response.body().getAsJsonObject()
                                                            .get("result").getAsString(), Toast.LENGTH_LONG).show();
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
                            popupWindow.dismiss();
                        }
                    });
                    txttimePicker.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Calendar mcurrentTime = Calendar.getInstance();
                            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                            int minute = mcurrentTime.get(Calendar.MINUTE);
                            TimePickerDialog mTimePicker;
                            mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                    String hour = selectedHour + "", minute = selectedMinute + "";
                                    if (selectedHour < 10) {
                                        hour = "0" + hour;
                                    }
                                    if (selectedMinute < 10) {
                                        minute = "0" + minute;
                                    }
                                    txttimePicker.setText(hour + ":" + minute);

                                }
                            }, hour, minute, true);//Yes 24 hour time
                            mTimePicker.setTitle("Chon thoi gian thi dau");
                            mTimePicker.show();

                        }
                    });
                    datePicker = popupView.findViewById(R.id.txt_create_day);
                    datePicker.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDatePickerDialog(v.getId(), datePicker.getText().toString().trim());
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Chi doi truong moi co the tao tran dau", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.menu_map_cancel:

                break;
            case R.id.menu_map_search:
                LayoutInflater inflaterSearch = (LayoutInflater)
                        getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupViewSearch = inflaterSearch.inflate(R.layout.popup_match_filter, null);
                // create the popup window
                int widthSearch = LinearLayout.LayoutParams.WRAP_CONTENT;
                int heightSearch = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusableSearch = true; // lets taps outside the popup also dismiss it
                PopupWindow popupWindowSearch = new PopupWindow(popupViewSearch, widthSearch, heightSearch, focusableSearch);
                popupWindowSearch.showAtLocation(getParentFragment().getView(), Gravity.CENTER_VERTICAL, 0, 0);
                datePicker = popupViewSearch.findViewById(R.id.filter_match_time);
                Spinner city, district;
                CheckBox oneStar, twoStar, threeStar, fourStar, fiveStar;

                city = popupViewSearch.findViewById(R.id.spinner_city);
                district = popupViewSearch.findViewById(R.id.spinner_district);

                oneStar = popupViewSearch.findViewById(R.id.onestar_checkbox);
                twoStar = popupViewSearch.findViewById(R.id.twostar_checkbox);
                threeStar = popupViewSearch.findViewById(R.id.threestar_checkbox);
                fourStar = popupViewSearch.findViewById(R.id.fourstar_checkbox);
                fiveStar = popupViewSearch.findViewById(R.id.fivestar_checkbox);

                city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (city.getItemAtPosition(position).toString()) {
                            case "Ha Noi":
                                ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, cd.getHanoiCity());
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                district.setAdapter(adapter);
                                break;
                            case "TP HCM":
                                break;
                            case "Hue":
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                datePicker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDatePickerDialog(v.getId(), datePicker.getText().toString().trim());
                    }
                });
                Button btnSearch = popupViewSearch.findViewById(R.id.btn_match_timkiem);
                btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String selCity = city.getSelectedItem().toString().toLowerCase();
                        String selDistrict = district.getSelectedItem().toString().toLowerCase();
                        String selDate = datePicker.getText().toString();
                        String query;
                        String queryStar = "";
                        if (oneStar.isChecked()) {
                            if (!queryStar.isEmpty()) {
                                queryStar += "OR rating = 1 ";
                            } else {
                                queryStar += "rating = 1 ";
                            }
                            Log.d("queryStar", queryStar);
                        }
                        if (twoStar.isChecked()) {
                            if (!queryStar.isEmpty()) {
                                queryStar += "OR rating = 2 ";
                            } else {
                                queryStar += "rating = 2 ";
                            }
                            Log.d("queryStar", queryStar);
                        }
                        if (threeStar.isChecked()) {
                            if (!queryStar.isEmpty()) {
                                queryStar += "OR rating = 3 ";
                            } else {
                                queryStar += "rating = 3 ";
                            }
                            Log.d("queryStar", queryStar);
                        }
                        if (fourStar.isChecked()) {
                            if (!queryStar.isEmpty()) {
                                queryStar += "OR rating = 4 ";
                            } else {
                                queryStar += "rating = 4 ";
                            }
                            Log.d("queryStar", queryStar);
                        }
                        if (fiveStar.isChecked()) {
                            if (!queryStar.isEmpty()) {
                                queryStar += "OR rating = 5 ";
                            } else {
                                queryStar += "rating = 5 ";
                            }
                            Log.d("queryStar", queryStar);
                        }
                        if (selDistrict.equalsIgnoreCase((String) district.getItemAtPosition(0))) {
                            if (selDate.equals("Click chon thoi gian")) {
                                Log.d("choose", 1 + " " + selDistrict);
                                query = "SELECT team.id as tid, team.name as tname, team.rating as trating, " +
                                        "team.phone as tphone, team.icon as ticon, team.win, team.draw, team.lose, " +
                                        "team.teamlocationid as tlid, street, district, city, latitude, longtitude, " +
                                        "draftedmatch_team.draftedmatchid as dmid, day, place,time, " +
                                        "draftedmatch_team.teamid as dmteamid, role " +
                                        "FROM team, teamlocation, draftedmatch, draftedmatch_team " +
                                        "WHERE teamlocation.id = team.teamlocationid  AND draftedmatch.id = " +
                                        "draftedmatch_team.draftedmatchid AND draftedmatch_team.teamid=team.id " +
                                        "AND NOT (team.id = " + myTeam.getId() + " AND " +
                                        "draftedmatch_team.role = 'create' " +
                                        "or draftedmatch_team.role = 'invite' or draftedmatch_team.role = 'invited'" +
                                        ")" +
                                        " AND draftedmatch.isaccepted=false ";
                            } else {
                                Log.d("choose", 2 + " " + selDistrict);
                                query = "SELECT team.id as tid, team.name as tname, team.rating as trating, " +
                                        "team.phone as tphone, team.icon as ticon, team.win, team.draw, team.lose, " +
                                        "team.teamlocationid as tlid, street, district, city, latitude, longtitude, " +
                                        "draftedmatch_team.draftedmatchid as dmid, day, place,time, " +
                                        "draftedmatch_team.teamid as dmteamid, role " +
                                        "FROM team, teamlocation, draftedmatch, draftedmatch_team " +
                                        "WHERE teamlocation.id = team.teamlocationid  AND draftedmatch.id = " +
                                        "draftedmatch_team.draftedmatchid AND draftedmatch_team.teamid=team.id " +
                                        "AND NOT (team.id = " + myTeam.getId() + " AND " +
                                        "draftedmatch_team.role = 'create' " +
                                        "or draftedmatch_team.role = 'invite' or draftedmatch_team.role = 'invited'" +
                                        ")" +
                                        " AND draftedmatch.isaccepted=false AND " +
                                        "draftedmatch.day=" + "'" + selDate + "' "
//                                        + "AND NOT day='" + selDate + "' "
                                ;
                            }
                        } else {
                            if (selDate.equals("Click chon thoi gian")) {
                                Log.d("choose", 3 + " " + selDistrict);
                                query = "SELECT team.id as tid, team.name as tname, team.rating as trating, " +
                                        "team.phone as tphone, team.icon as ticon, team.win, team.draw, team.lose, " +
                                        "team.teamlocationid as tlid, street, district, city, latitude, longtitude, " +
                                        "draftedmatch_team.draftedmatchid as dmid, day, place,time, " +
                                        "draftedmatch_team.teamid as dmteamid, role " +
                                        "FROM team, teamlocation, draftedmatch, draftedmatch_team " +
                                        "WHERE teamlocation.id = team.teamlocationid  AND draftedmatch.id = " +
                                        "draftedmatch_team.draftedmatchid AND draftedmatch_team.teamid=team.id " +
                                        "AND NOT (team.id = " + myTeam.getId() + " AND " +
                                        "draftedmatch_team.role = 'create' " +
                                        "or draftedmatch_team.role = 'invite' or draftedmatch_team.role = 'invited'" +
                                        ")" +
                                        " AND draftedmatch.isaccepted=false AND " +
                                        "district='" + selDistrict + "' ";
                            } else {
                                Log.d("choose", 4 + " " + selDistrict);
                                query = "SELECT team.id as tid, team.name as tname, team.rating as trating, " +
                                        "team.phone as tphone, team.icon as ticon, team.win, team.draw, team.lose, " +
                                        "team.teamlocationid as tlid, street, district, city, latitude, longtitude, " +
                                        "draftedmatch_team.draftedmatchid as dmid, day, place,time, " +
                                        "draftedmatch_team.teamid as dmteamid, role " +

                                        "FROM team, teamlocation, draftedmatch, draftedmatch_team " +
                                        "WHERE teamlocation.id = team.teamlocationid  AND draftedmatch.id = " +
                                        "draftedmatch_team.draftedmatchid AND draftedmatch_team.teamid=team.id " +
                                        "AND NOT (team.id = " + myTeam.getId() + " AND " +
                                        "draftedmatch_team.role = 'create' " +
                                        "or draftedmatch_team.role = 'invite' or draftedmatch_team.role = 'invited'" +
                                        ")" +
                                        " AND draftedmatch.isaccepted=false AND " +
                                        "draftedmatch.day=" + "'" + selDate + "' " +
//                                        "AND NOT day='" + selDate + "' " +
                                        "AND district='" + selDistrict + "' ";
                            }
                        }
                        if (!queryStar.isEmpty()) {
                            query += " AND " + queryStar +
                                    " ORDER BY draftedmatch_team.draftedmatchid DESC, role ASC";

                        } else {
                            query += "ORDER BY draftedmatch_team.draftedmatchid DESC, role ASC";
                        }
                        JSONObject data = new JSONObject();
                        try {
                            data.put("query", query);
                            MyService myRetrofit = RetrofitClient.getInstance(baseURL).create(MyService.class);
                            Call<JsonObject> callBack = myRetrofit.searchForMatches(data);
                            callBack.enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    if (response.isSuccessful() &&
                                            response.body().get("result").getAsJsonArray().size() > 0
                                                   ) {
                                        arrTeams.clear();
                                        arrDM.clear();
                                        markers.clear();

                                        JsonArray arrResult = response.body().get("result").getAsJsonArray();
                                        for (JsonElement e : arrResult) {
                                            JsonObject teamlocation = e.getAsJsonObject().get("teamlocation").getAsJsonObject();
                                            TeamLocation tl = new TeamLocation(teamlocation.get("tlid").getAsInt(),
                                                    teamlocation.get("street").getAsString(),
                                                    teamlocation.get("district").getAsString(),
                                                    teamlocation.get("city").getAsString(),
                                                    teamlocation.get("latitude").getAsDouble(),
                                                    teamlocation.get("longtitude").getAsDouble());
                                            JsonObject team = e.getAsJsonObject().get("team").getAsJsonObject();
                                            Team team1 = new Team(
                                                    team.get("tid").getAsInt(),
                                                    team.get("tname").getAsString(),
                                                    team.get("ticon").getAsString(),
                                                    team.get("trating").getAsDouble(),
                                                    new ArrayList<Player>(),
                                                    team.get("win").getAsInt(),
                                                    team.get("draw").getAsInt(),
                                                    team.get("lose").getAsInt(), false, tl
                                            );
                                            JsonObject match = e.getAsJsonObject().get("match").getAsJsonObject();
                                            DraftedMatch dm = new DraftedMatch(
                                                    match.get("dmid").getAsInt(),
                                                    false, false, match.get("place").getAsString(),
                                                    java.sql.Date.valueOf(match.get("day").getAsString()),
                                                    match.get("time").getAsString(),
                                                    false
                                            );
                                            DraftMatchTeam dmt = new DraftMatchTeam(team1, dm, match.get("role").getAsString(),
                                                    false, 0);
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
                                            if (arrTeams.size() == 0 && team1.getId() != myTeam.getId()) {
                                                arrTeams.add(team1);
                                            } else {
                                                for (int i = 0; i < arrTeams.size(); i++) {
                                                    if (arrTeams.get(i).getId() == team1.getId()) {
                                                        break;
                                                    }
                                                    if (arrTeams.get(i).getId() != team1.getId() && team1.getId() != myTeam.getId()
                                                            && i == arrTeams.size() - 1) {
                                                        arrTeams.add(team1);
                                                    }
                                                }
                                            }


                                        }
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                ggMap.clear();
                                                LatLng coord = null;
                                                for (Team t : arrTeams) {
                                                    coord = new LatLng(t.gettLocation().getLatitude(), t.gettLocation().getLongtitude());
                                                    MarkerOptions markerOptions = new MarkerOptions();
                                                    markerOptions.position(coord).
                                                            title(t.getName())
                                                            .snippet(t.getRating() + "");
                                                    InfoWindowMap infoWindowMap = new InfoWindowMap(getContext());

                                                    ggMap.setInfoWindowAdapter(infoWindowMap);
                                                    Marker m = ggMap.addMarker(markerOptions);
                                                    m.setTag(t);
                                                    markers.add(m);
                                                    m.showInfoWindow();
                                                }
                                                ggMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 15.0f));
                                            }
                                        });

                                    }
                                    else {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getContext(), "Khong co tran phu hop", Toast.LENGTH_SHORT).show();
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
                        popupWindowSearch.dismiss();
                    }
                });

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDatePickerDialog(int callerId, String dateText) {
        this.callerId = callerId;
        Date date = null;

        try {
            if (dateText.equals(""))
                date = new Date();
            else
                date = dateFormatter.parse(dateText);
        } catch (Exception exp) {
            // In case of expense initializa date with new Date
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH); // calendar month 0-11
        int day = calendar.get(Calendar.DATE);
        // date picker initialization
        DatePickerDialog datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                handleOnDateSet(year, month, day);
            }
        }, year, month, day);
        datePicker.setTitle("My date picker");
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", datePicker);
        datePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Cancel button clicked
            }
        });
        datePicker.show();
    }

    public void handleOnDateSet(int year, int month, int day) {
        Date date = new GregorianCalendar(year, month, day).getTime();

        String formatedDate = dateFormatter.format(date);
        switch (callerId) {
            case R.id.filter_match_time:
            case R.id.txt_create_day:
                datePicker.setText(formatedDate);
                break;
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ggMap.setMyLocationEnabled(true);
                }
                break;
        }
    }

}