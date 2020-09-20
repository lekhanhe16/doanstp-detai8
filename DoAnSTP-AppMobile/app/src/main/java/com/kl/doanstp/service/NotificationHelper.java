package com.kl.doanstp.service;
import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.kl.doanstp.R;
import com.kl.doanstp.model.NotificationID;

public class NotificationHelper {
    public static String GROUP_NEW_MATCH = "group new match";
    public static void displayNewMatchNotification(Context ctx, String title, String content, String channelID){
        NotificationCompat.Builder mNotiBuilder =
                new NotificationCompat.Builder(ctx, channelID)
                    .setContentTitle(title)
                    .setContentText(content)
                        .setSmallIcon(R.drawable.ic_baseline_check_circle_24)

                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notiMgr = NotificationManagerCompat.from(ctx);
        notiMgr.notify(NotificationID.getID(), mNotiBuilder.build());
    }

    public static void displayPlayerApplicationNoti(Context ctx, String title, String content, String channelID){
        NotificationCompat.Builder mNotiBuilder =
                new NotificationCompat.Builder(ctx, channelID)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setSmallIcon(R.drawable.ic_baseline_check_circle_24)

                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notiMgr = NotificationManagerCompat.from(ctx);
        notiMgr.notify(NotificationID.getID(), mNotiBuilder.build());
    }
}
