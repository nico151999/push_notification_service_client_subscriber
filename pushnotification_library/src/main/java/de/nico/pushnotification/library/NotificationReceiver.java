package de.nico.pushnotification.library;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = NotificationReceiver.class.getSimpleName();
    private static final String ACTION_SHOW_NOTIFICATION = "de.nico.pushnotification.library.action.SHOW_NOTIFICATION";
    private static final String TITLE_KEY = "title";
    private static final String CONTENT_KEY = "content";
    private static final String ICON_KEY = "icon";
    private static final String URI_KEY = "uri";
    private static final String ICON_META = "de.nico.pushnotification.library.default_notification_icon";
    private static final String NOTIFICATION_CHANNEL_META = "de.nico.pushnotification.library.notification_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_SHOW_NOTIFICATION.equals(intent.getAction())) {
            Log.i(TAG, "Receiving notification");
            ApplicationInfo ai;
            try {
                ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Cannot get application info");
                return;
            }
            String channelId = getNotificationChannel(ai);
            context.getSystemService(NotificationManager.class).createNotificationChannel(
                    new NotificationChannel(
                            channelId,
                            "PushNotificationViewer",
                            NotificationManager.IMPORTANCE_DEFAULT
                    )
            );
            NotificationManagerCompat nm = NotificationManagerCompat.from(context);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    context,
                    channelId
            ).setSmallIcon(
                    getSmallIcon(ai)
            ).setPriority(
                    NotificationCompat.PRIORITY_DEFAULT
            ).setContentTitle(
                    intent.hasExtra(TITLE_KEY) ?
                            intent.getStringExtra(TITLE_KEY) :
                            ""
            ).setContentText(
                    intent.hasExtra(CONTENT_KEY) ?
                            intent.getStringExtra(CONTENT_KEY) :
                            ""
            );
            if (intent.hasExtra(ICON_KEY)) {
                try {
                    byte[] b = Base64.decode(intent.getStringExtra(ICON_KEY), Base64.DEFAULT);
                    builder.setLargeIcon(
                            BitmapFactory.decodeByteArray(b, 0, b.length)
                    );
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "The submitted base64 icon has an improper format");
                }
            }
            if (intent.hasExtra(URI_KEY)) {
                builder
                        .setAutoCancel(true)
                        .setContentIntent(
                                PendingIntent.getActivity(
                                        context,
                                        (int) System.nanoTime(),
                                        new Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse(intent.getStringExtra(URI_KEY))
                                        ),
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                )
                        );
            }
            nm.notify(
                    (int)System.nanoTime(),
                    builder.build()
            );
        }
    }

    private int getSmallIcon(ApplicationInfo ai) {
        int icon = ai.metaData.getInt(ICON_META);
        if (icon == 0) {
            icon = ai.icon;
            if (icon == 0) {
                return android.R.drawable.sym_def_app_icon;
            }
        }
        return icon;
    }

    private String getNotificationChannel(ApplicationInfo ai) {
        String channel = ai.metaData.getString(NOTIFICATION_CHANNEL_META);
        if (channel == null) {
            return ai.packageName + ".pushnotification";
        }
        return channel;
    }
}
