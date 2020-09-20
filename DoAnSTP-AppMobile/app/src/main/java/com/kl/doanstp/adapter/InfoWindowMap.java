package com.kl.doanstp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.kl.doanstp.R;
import com.kl.doanstp.model.Team;

public class InfoWindowMap implements GoogleMap.InfoWindowAdapter {
    private Context context;
    public InfoWindowMap (Context context){
        this.context = context;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.team_info_window, null);
        TextView teamName = view.findViewById(R.id.team_name_infowindow);
        TextView teamRate = view.findViewById(R.id.rate_infowindow);
        Team team = (Team) marker.getTag();
        teamName.setText(marker.getTitle());
        teamRate.setText(marker.getSnippet());
        return view;
    }
}
