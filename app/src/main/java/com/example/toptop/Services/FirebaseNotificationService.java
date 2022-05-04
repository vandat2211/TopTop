package com.example.toptop.Services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;


import com.example.toptop.ChatActivity;
import com.example.toptop.Models.userObject;
import com.example.toptop.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class FirebaseNotificationService extends FirebaseMessagingService {
    public static final String TAG = FirebaseNotificationService.class.getName();
    private userObject user = new userObject();

    @Override
    public void onMessageReceived(@NonNull RemoteMessage messages) {
        super.onMessageReceived(messages);
        String title = messages.getNotification().getTitle();
        String body = messages.getNotification().getBody();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "CHAT");
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setSmallIcon(R.drawable.avatar);
        Intent intent = null;
        if (messages.getData().get("type").equalsIgnoreCase("mes")) {
            intent = new Intent(this, ChatActivity.class);
            intent.putExtra("hisUid", messages.getData().get("MyUid"));
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(123, builder.build());
    }
}