package de.nico.simplechannelsubscriber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.subscribe).setOnClickListener((view) -> {
            try {
                subscribe(((EditText) findViewById(R.id.topic)).getText().toString());
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void subscribe(String topic) throws PackageManager.NameNotFoundException {
        String packageName = "de.nico.pushnotification.servicetester";
        String subscriptionReceiver = packageName + ".ChannelSubscriptionReceiver";
        String extraSubscriptionString = "EXTRA_SUBSCRIPTION";
        String actionSubscribeString = "ACTION_SUBSCRIBE_NOTIFICATION_CHANNEL";
        ComponentName component =
                new ComponentName(packageName,subscriptionReceiver);

        String subscriptionKey = getString(packageName, extraSubscriptionString);
        String action = getString(packageName, actionSubscribeString);

        if (subscriptionKey != null && action != null) {
            sendBroadcast(
                    new Intent()
                            .setAction(action)
                            .putExtra(subscriptionKey, topic)
                            .setComponent(component)
            );
        }
    }

    private String getString(String packageName, String stringKey) throws PackageManager.NameNotFoundException {
        PackageManager pm = getPackageManager();
        int extraSubscriptionId = pm.getResourcesForApplication(packageName).getIdentifier(
                packageName + ":string/" + stringKey,
                null,
                null
        );
        if (0 != extraSubscriptionId) {
            return (String) pm.getText(packageName, extraSubscriptionId, null);
        } else {
            return null;
        }
    }
}