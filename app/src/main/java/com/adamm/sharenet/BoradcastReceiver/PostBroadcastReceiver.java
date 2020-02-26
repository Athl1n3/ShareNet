package com.adamm.sharenet.BoradcastReceiver;

import android.app.Notification;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.adamm.sharenet.Database.AppDatabase;
import com.adamm.sharenet.R;
import com.adamm.sharenet.entities.Post;

import static com.adamm.sharenet.Services.PostService.CHANNEL_ID;

public class PostBroadcastReceiver extends BroadcastReceiver {
    private static final String KEY_TEXT_REPLY = "POST_KEY";
    private static int notificationId = 1;
    private AppDatabase mDatabase;
    @Override
    public void onReceive(Context context, Intent intent) {
        getMessageText(intent);

    }
    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            mDatabase.postDao().addPost(
                    new Post(mDatabase.curr_user.uid, mDatabase.curr_user.username, "From Foreground", String.valueOf(remoteInput.getCharSequence(KEY_TEXT_REPLY))));

            Notification repliedNotification = new Notification.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_delete_24px)
                    .setContentText("Posted")
                    .build();

            // Issue the new notification.
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(notificationId, repliedNotification);
            return remoteInput.getCharSequence(KEY_TEXT_REPLY);
        }
        return null;
    }
}
