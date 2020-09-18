package com.kl.doanstp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kl.doanstp.view.MainActivity2;
import com.kl.doanstp.view.MyTeamFragment;

import static android.content.ContentValues.TAG;

public class MyFCMService extends FirebaseMessagingService {
    public MyFCMService() {
    }

//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
//        Log.d(TAG, "Refreshed token: " + s);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.


        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            String title = remoteMessage.getNotification().getTitle();
            String content = remoteMessage.getNotification().getBody();
//            String channelid = remoteMessage.getNotification()
            if (title.equalsIgnoreCase("Tran dau moi")){
                NotificationHelper.displayNewMatchNotification(getApplicationContext(), title, content,
                        MainActivity2.CHANNEL_NEW_MATCH);
                Intent intent = new Intent();
                intent.setAction("UPDATE_FLOAT_BUTTON_NOTI");
                sendBroadcast(intent);
            }

            else if (title.equalsIgnoreCase("Yeu cau tham gia")){
                NotificationHelper.displayPlayerApplicationNoti(getApplicationContext(), title, content,
                        MainActivity2.CHANNEL_APPLY);

                Intent intent = new Intent();
                intent.setAction("UPDATE_CEN_NUM");
                sendBroadcast(intent);
            }
            else if (title.equalsIgnoreCase("Tham gia doi bong moi")){
                NotificationHelper.displayPlayerApplicationNoti(getApplicationContext(), title, content,
                        MainActivity2.CHANNEL_APPLY);
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

}
