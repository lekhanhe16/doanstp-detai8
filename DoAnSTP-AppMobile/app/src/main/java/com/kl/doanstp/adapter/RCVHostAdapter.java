package com.kl.doanstp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kl.doanstp.R;
import com.kl.doanstp.model.DraftMatchTeam;
import com.kl.doanstp.model.DraftedMatch;
import com.kl.doanstp.model.Player;
import com.kl.doanstp.model.Team;

import java.util.ArrayList;

public class RCVHostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<DraftedMatch> dMatch;
    Context context;
    public final int INVITE = 1;
    public final int CREATE = 2;
    DraftedMatch dm;
    Player thisPlayer;
    Team myTeam;

    public RCVHostAdapter(Context context, ArrayList<DraftedMatch> dm, Player player, Team team) {
        this.context = context;
        this.dMatch = dm;
        this.thisPlayer = player;
        this.myTeam = team;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == INVITE) {
            view = LayoutInflater.from(this.context).inflate(R.layout.host_match, parent, false);
            return new RecyclerViewHolder1(view);
        } else if (viewType == CREATE) {
            view = LayoutInflater.from(this.context).inflate(R.layout.host_match2, parent, false);
            return new RecyclerViewHolder2(view);
        }

        return null;
    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        dm = this.dMatch.get(position);
        if (holder.getItemViewType() == INVITE) {
            RecyclerViewHolder1 vh1 = (RecyclerViewHolder1) holder;
            if (!dm.isEnd()) {

                vh1.content1.setText("Doi ban moi " + dm.getDmt().get(1).getTeam().getName() + " da");

                if (dm.isDenied() && !dm.getDmt().get(1).isReady()) {
                    vh1.accept1.setImageResource(R.drawable.ic_baseline_cancel_24);
                    vh1.status1.setText("Bi tu choi");
                    vh1.status1.setTextColor(Color.RED);
                }
                if (dm.getDmt().get(1).isReady()) {
                    vh1.accept1.setImageResource(R.drawable.ic_baseline_check_circle_24);
                    vh1.status1.setText("OK");
                    vh1.status1.setTextColor(Color.GREEN);
                }
            } else if (dm.isEnd()) {
                // update code here
                if (dm.getDmt().get(0).getResstt() == 1){
                    vh1.content1.setText(Html.fromHtml(dm.getDmt().get(0).getTeam().getName() + " " +
                            dm.getScoreTable().getFinalScore() + " " + dm.getDmt().get(1).getTeam().getName() +
                            " " + getColoredSpanned("Thua", "#FF0000"), Html.FROM_HTML_MODE_LEGACY));
                }
                else if (dm.getDmt().get(0).getResstt() == 2){
                    vh1.content1.setText(Html.fromHtml(dm.getDmt().get(0).getTeam().getName() + " " +
                            dm.getScoreTable().getFinalScore() + " " + dm.getDmt().get(1).getTeam().getName() +
                            " " + getColoredSpanned("Hoa", "#FFFF00"), Html.FROM_HTML_MODE_LEGACY));
                }
                else if (dm.getDmt().get(0).getResstt() == 3){
                    vh1.content1.setText(Html.fromHtml(dm.getDmt().get(0).getTeam().getName() + " " +
                            dm.getScoreTable().getFinalScore() + " " + dm.getDmt().get(1).getTeam().getName() +
                            " " + getColoredSpanned("Thang", "#00FF00"), Html.FROM_HTML_MODE_LEGACY));
                }
                vh1.accept1.setImageResource(R.drawable.ic_baseline_check_circle_24);
                vh1.status1.setText("Da ket thuc");
                vh1.status1.setTextColor(Color.GREEN);
            }
            vh1.time1.setText(dm.getDay().toString() + " luc " + dm.getTime());
            vh1.place1.setText(dm.getPlace());

        } else {
            RecyclerViewHolder2 vh2 = (RecyclerViewHolder2) holder;
            if (!dm.isEnd()) {
                vh2.content2.setText("Doi ban da tao tran dau moi");
                vh2.time2.setText(dm.getDay().toString() + " luc " + dm.getTime());
                vh2.place2.setText(dm.getPlace());

                boolean isClick = false;
                if (dm.isAccepted()) {
                    vh2.accept2.setImageResource(R.drawable.ic_baseline_check_circle_24);
                    vh2.status2.setText("OK");
                    vh2.status2.setTextColor(Color.GREEN);
                    vh2.content2.setText(dm.getDmt().get(0).getTeam().getName() + " da tham gia tran dau.");
                    vh2.parent.setClickable(false);
                } else if (dm.isDenied() && !dm.isAccepted()) {
                    vh2.accept2.setImageResource(R.drawable.ic_baseline_cancel_24);
                    vh2.status2.setText("Bi huy");
                    vh2.status2.setTextColor(Color.RED);
                    vh2.parent.setClickable(false);
                } else {
                    for (DraftMatchTeam dt : dm.getDmt()) {
                        if (dt.isReady()) {
                            isClick = true;
                            break;
                        }
                    }
                    ExpandLayoutAdapter adapter = new ExpandLayoutAdapter(context, dm.getDmt(), isClick, thisPlayer);
                    vh2.expandedLayout.setAdapter(adapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                            LinearLayoutManager.VERTICAL, false);
                    vh2.expandedLayout.setLayoutManager(linearLayoutManager);
                    vh2.expandedLayout.setVisibility(dm.isExpanded() ? View.VISIBLE : View.GONE);
                }
            } else if (dm.isEnd()) {
                // update code here
                if (dm.getDmt().get(0).getResstt() == 3){
                    vh2.content2.setText(Html.fromHtml(myTeam.getName() + " " +
                            dm.getScoreTable().getFinalScore() + " " + dm.getDmt().get(0).getTeam().getName() +
                            " " + getColoredSpanned("Thua", "#FF0000"), Html.FROM_HTML_MODE_LEGACY));
                }

                else if (dm.getDmt().get(0).getResstt() == 2){
                    vh2.content2.setText(Html.fromHtml(myTeam.getName() + " " +
                            dm.getScoreTable().getFinalScore() + " " + dm.getDmt().get(0).getTeam().getName() +
                            " " + getColoredSpanned("Hoa", "#FFFF00"), Html.FROM_HTML_MODE_LEGACY));
                }
                else if (dm.getDmt().get(0).getResstt() == 1){
                    vh2.content2.setText(Html.fromHtml(myTeam.getName() + " " +
                            dm.getScoreTable().getFinalScore() + " " + dm.getDmt().get(0).getTeam().getName() +
                            " " + getColoredSpanned("Thang", "#00FF00"), Html.FROM_HTML_MODE_LEGACY));
                }
                vh2.accept2.setImageResource(R.drawable.ic_baseline_check_circle_24);
                vh2.status2.setText("Da ket thuc");
                vh2.status2.setTextColor(Color.GREEN);
            }
            vh2.time2.setText(dm.getDay().toString() + " luc " + dm.getTime());
            vh2.place2.setText(dm.getPlace());
        }

    }

    @Override
    public int getItemViewType(int position) {
        try {
            if (dMatch.get(position).getDmt().get(0).getRole().equals("invite")) {
                return INVITE;
            } else if (dMatch.get(position).getDmt().get(0).getRole().equals("create")) {
                return CREATE;
            }
        } catch (IndexOutOfBoundsException e) {

        }
        return CREATE;
    }

    @Override
    public int getItemCount() {
        return this.dMatch.size();
    }

    public class RecyclerViewHolder1 extends RecyclerView.ViewHolder {
        TextView content1, time1, place1, status1;
        ImageView accept1;


        public RecyclerViewHolder1(@NonNull View itemView) {
            super(itemView);
            content1 = itemView.findViewById(R.id.item_content);
            time1 = itemView.findViewById(R.id.item_thoigian);
            place1 = itemView.findViewById(R.id.item_diadiem);
            status1 = itemView.findViewById(R.id.item_status);
            accept1 = itemView.findViewById(R.id.item_agree);

        }
    }

    public class RecyclerViewHolder2 extends RecyclerView.ViewHolder {
        RecyclerView expandedLayout;
        TextView content2, time2, place2, status2;
        ImageView accept2;
        ConstraintLayout parent;


        public RecyclerViewHolder2(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent_hostmatch2);
            content2 = itemView.findViewById(R.id.item_content2);
            time2 = itemView.findViewById(R.id.item_thoigian2);
            place2 = itemView.findViewById(R.id.item_diadiem2);
            status2 = itemView.findViewById(R.id.item_status2);
            accept2 = itemView.findViewById(R.id.item_agree2);
            expandedLayout = itemView.findViewById(R.id.item_expand_layout);

            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!dMatch.get(getAdapterPosition()).isEnd()){
                        dMatch.get(getAdapterPosition())
                                .setExpanded(!dMatch.get(getAdapterPosition()).isExpanded());
                        notifyItemChanged(getAdapterPosition());
                    }
                    else {

                    }
                }
            });

        }
    }
}
