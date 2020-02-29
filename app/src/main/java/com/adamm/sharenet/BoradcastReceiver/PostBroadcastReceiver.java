package com.adamm.sharenet.BoradcastReceiver;

import android.app.Notification;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationManagerCompat;

import com.adamm.sharenet.Database.AppDatabase;
import com.adamm.sharenet.R;
import com.adamm.sharenet.Services.PostService;
import com.adamm.sharenet.entities.Post;

import static com.adamm.sharenet.Services.PostService.CHANNEL_ID;

public class PostBroadcastReceiver extends BroadcastReceiver {
    private static final String KEY_TEXT_REPLY = "POST_KEY";
    private AppDatabase mDatabase;
    @Override
    public void onReceive(Context context, Intent intent) {
        mDatabase = AppDatabase.getAppDatabase(context);
        getMessageText(context,intent);
    }

    private void getMessageText(Context context, Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            mDatabase.postDao().addPost(
                    new Post(AppDatabase.getCurr_user().uid, AppDatabase.getCurr_user().username, "From Foreground", String.valueOf(remoteInput.getCharSequence(KEY_TEXT_REPLY))));

            Notification repliedNotification = new Notification.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_toggle_star_24).setContentText("Posted")
                    .setRemoteInputHistory(remoteInput.getCharSequenceArray(KEY_TEXT_REPLY)).setTimeoutAfter(1)
                    .build();
            // Issue the new notification reply that post was posted successfully.
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(AppDatabase.getNotificationId(), repliedNotification);

            //AppDatabase.setNotificationId(AppDatabase.getNotificationId()+1);
           // AppDatabase.postService.stopSelf();
            //AppDatabase.postService.startForegroundService(new Intent(context, PostService.class));
        }
    }
}