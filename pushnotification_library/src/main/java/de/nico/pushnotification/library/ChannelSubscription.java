package de.nico.pushnotification.library;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class ChannelSubscription {
    private static final String PACKAGE_NAME = "de.nico.pushnotification.servicetester";
    private static final String RECEIVER_CLASS = "receiver.ChannelSubscriptionReceiver";
    private static final String RECEIVER_PERMISSION = "de.nico.pushnotification.servicetester.permission.SEND_NOTIFICATION";
    private static final String SUBSCRIPTION_CHANNEL_KEY = "SUBSCRIPTION_CHANNEL_KEY";
    private static final String ACTION = "de.nico.pushnotification.servicetester.action.SUBSCRIBE_NOTIFICATION_CHANNEL";
    private static final String SUBSCRIPTION_PACKAGE_KEY = "SUBSCRIPTION_PACKAGE_KEY";

    private String mChannel;

    public void subscribe(Context context) {
        ComponentName component = new ComponentName(PACKAGE_NAME, PACKAGE_NAME + "." + RECEIVER_CLASS);
        context.sendBroadcast(
                new Intent()
                        .setAction(ACTION)
                        .putExtra(SUBSCRIPTION_CHANNEL_KEY, mChannel)
                        .putExtra(SUBSCRIPTION_PACKAGE_KEY, context.getPackageName())
                        .setComponent(component),
                RECEIVER_PERMISSION
        );
    }

    private ChannelSubscription(String channel) {
        mChannel = channel;
    }

    public static class ChannelSubscriptionBuilder {
        private String mmChannel;

        public ChannelSubscriptionBuilder setChannel(String channel) {
            mmChannel = channel;
            return this;
        }

        public ChannelSubscription build() {
            if (mmChannel == null) {
                throw new IllegalStateException("The channel must not be null");
            }
            return new ChannelSubscription(mmChannel);
        }
    }
}
