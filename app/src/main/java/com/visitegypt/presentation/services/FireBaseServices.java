package com.visitegypt.presentation.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.visitegypt.R;
import com.visitegypt.presentation.home.parent.Home;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class FireBaseServices extends FirebaseMessagingService {
    private NotificationManager notificationManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        createNotificationChannel();
        try {
            showNotification(getPendingIntent(),message, Random.class.newInstance().nextInt());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(
                getResources().getString(R.string.default_notification_channel_id),
                name,
                importance
        );
        channel.setDescription(description);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.GREEN);
        channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null);
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

    }

    private void showNotification(PendingIntent pendingIntent,RemoteMessage remoteMessage, int id) {
        Bitmap image = null;
        try {
            URL url = new URL(remoteMessage.getData().get("image"));
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            System.out.println(e);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark_normal)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("body"))
                .setAutoCancel(true)
                .setLargeIcon(image)
                .setSubText("Incoming Client")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(remoteMessage.getData().get("title")))
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(image)
                        .bigLargeIcon(image))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setContentIntent(pendingIntent);
        notificationManager.notify(id, notificationBuilder.build());

    }
    public PendingIntent getPendingIntent()
    {
        Intent intent = new Intent(this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return  PendingIntent.getActivity(this, 1410, intent, PendingIntent.FLAG_ONE_SHOT);
    }



}
