package com.adamm.sharenet.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.adamm.sharenet.BoradcastReceiver.PostBroadCastReceiver;
import com.adamm.sharenet.MainActivity;
import com.adamm.sharenet.R;

public class PostService extends Service {

    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private final String Action = "adamm.ShareNet.Post";

    private static final String KEY_TEXT_REPLY = "POST_KEY";



    public PostService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Intent postIntent = new Intent(this, PostBroadCastReceiver.class);
        postIntent.setAction(Action);
        postIntent.putExtra(CHANNEL_ID, 0);
        PendingIntent postPendingIntent =
                PendingIntent.getBroadcast(this, 0, postIntent, 0);





        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("ShareNet Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.firebase_lockup_400)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_toggle_star_24,"Post",postPendingIntent)
                .build();
        startForeground(1, notification);
        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Share a post",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

}


