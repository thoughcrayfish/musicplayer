package com.example.musicplayerapp.ui.Notifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.musicplayerapp.R;

public class NotificationPanel {

    private Context ctx;
    private NotificationManager mNotificationManager;

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public NotificationPanel(Context ctx) {
        super();
        this.ctx = ctx;
        String ns = Context.NOTIFICATION_SERVICE;
        mNotificationManager = (NotificationManager) ctx.getSystemService(ns);
        CharSequence tickerText = "Shortcuts";
        long when = System.currentTimeMillis();
        Notification.Builder builder = new Notification.Builder(ctx);

        Notification notification = builder.getNotification();
        notification.when = when;
        notification.tickerText = tickerText;
        notification.icon = R.drawable.ic_menu_camera;

        RemoteViews contentView = new RemoteViews(ctx.getPackageName(), R.layout.layout_notification);

        //set the button listeners
        setListeners(contentView);

        notification.contentView = contentView;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        CharSequence contentTitle = "From Shortcuts";
        mNotificationManager.notify(548853, notification);
    }

    public void setListeners(RemoteViews view) {
        //radio listener
        Intent playPause = new Intent(ctx, NotificationReturnSlot.class);
        playPause.putExtra("DO", "playPause");
        PendingIntent pPlayPause = PendingIntent.getActivity(ctx, 0, playPause, 0);
        view.setOnClickPendingIntent(R.id.imageButton_notification_playPause, pPlayPause);

        //volume listener
        Intent next = new Intent(ctx, NotificationReturnSlot.class);
        next.putExtra("DO", "next");
        PendingIntent pNext = PendingIntent.getActivity(ctx, 1, next, 0);
        view.setOnClickPendingIntent(R.id.imageButton_notification_next, pNext);
    }

}