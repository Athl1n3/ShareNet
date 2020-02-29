package com.adamm.sharenet.BoradcastReceiver;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import com.adamm.sharenet.Database.AppDatabase;
import com.adamm.sharenet.MainActivity;
import com.adamm.sharenet.R;
import com.adamm.sharenet.Services.PostService;
import com.adamm.sharenet.entities.Post;

import java.util.LinkedList;
import java.util.List;

import static com.adamm.sharenet.Services.PostService.CHANNEL_ID;

public class PostBroadcastReceiver extends BroadcastReceiver {
    private static final String KEY_TEXT_REPLY = "POST_KEY";
    private AppDatabase mDatabase;
    private static int notifier = 1;
    private static List<CharSequence> responseHistory = new LinkedList<>();
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
            responseHistory.add(0, remoteInput.getCharSequence(KEY_TEXT_REPLY));

            Notification repliedNotification = new Notification.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_toggle_star_24).setContentText("Posted: " + remoteInput.getCharSequence(KEY_TEXT_REPLY))
                    //.setRemoteInputHistory(remoteInput.getCharSequenceArray(KEY_TEXT_REPLY))
                    .build();
            // Issue the new notification reply that post was posted successfully.
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notifier += 1;
            notificationManager.notify(notifier, repliedNotification);//Make a new notification box for "Posted message"

            if(!responseHistory.isEmpty()) {
                CharSequence[] history = new CharSequence[responseHistory.size()];
                inlineReplyNotification(context, responseHistory.toArray(history));
            }
        }
    }

    private void inlineReplyNotification(Context context, CharSequence[] history)// Re-Issue notification, we use the same ID so we keep using the same notification box [Foreground service notification]
    {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);//Launch main activity when tapped
        // ///////////----------------//
        //Building reply objects
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel("New post text")
                .build();//Make Remote input object

        Intent postIntent = new Intent(context, PostBroadcastReceiver.class);
        PendingIntent postReplyPendingIntent = PendingIntent.getBroadcast(context, 0, postIntent, PendingIntent.FLAG_UPDATE_CURRENT);//Launch broadcast receiver for post replies

        NotificationCompat.Action replyAction =
                new NotificationCompat.Action.Builder(R.drawable.ic_image_edit,
                        "New Post", postReplyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();
        ////////////////////////

        //**Building the whole notification**//
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_toggle_star_outline_24)
                .setContentTitle("ShareNet Post Service")
                .setContentText("Type in your post")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent).setRemoteInputHistory(history)// Launch mainActivity on click
                .addAction(replyAction);//New post reply action

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(AppDatabase.getNotificationId(), notification.build());//Re-Issue notification number 1
    }
}