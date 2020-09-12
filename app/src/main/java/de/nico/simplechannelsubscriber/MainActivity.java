package de.nico.simplechannelsubscriber;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import de.nico.pushnotification.library.ChannelSubscription;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.subscribe).setOnClickListener((view) -> {
            ChannelSubscription cs = new ChannelSubscription.ChannelSubscriptionBuilder()
                    .setChannel(((EditText) findViewById(R.id.channel)).getText().toString())
                    .build();
            cs.subscribe(this);
        });
    }
}