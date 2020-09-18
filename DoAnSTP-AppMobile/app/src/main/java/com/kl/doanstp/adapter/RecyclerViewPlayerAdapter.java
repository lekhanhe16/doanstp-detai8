package com.kl.doanstp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kl.doanstp.R;
import com.kl.doanstp.model.Player;

import java.util.ArrayList;

public class RecyclerViewPlayerAdapter extends RecyclerView.Adapter<RecyclerViewPlayerAdapter.RecyclerViewHolder>  {
    ArrayList<Player> arrPlayer;
    Context context;
    Player p;
    public RecyclerViewPlayerAdapter(ArrayList<Player> players, Context c){
        this.arrPlayer = players;
        this.context = c;
    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.card_view_player, parent, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        p = this.arrPlayer.get(position);
        holder.playerName.setText(p.getName());
        if (p.getIcon().isEmpty()){
            holder.playerAvatar.setImageResource(R.drawable.ball);
        }
        else {

        }
        if(p.isCaptain()){
            holder.iconCaptain.setImageResource(R.drawable.captainicon);
        }
    }


    @Override
    public int getItemCount() {
        return this.arrPlayer.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView playerName;
        ImageView playerAvatar, iconCaptain;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            playerAvatar = itemView.findViewById(R.id.player_icon);
            playerName = itemView.findViewById(R.id.player_name);
            iconCaptain = itemView.findViewById(R.id.icon_captain);
        }
    }
}
