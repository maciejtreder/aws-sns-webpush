package com.maciejtreder.aws.webpush;

import com.google.gson.Gson;
import com.maciejtreder.aws.webpush.dao.LogsStore;
import com.maciejtreder.aws.webpush.dao.SubscriptionStore;
import com.maciejtreder.aws.webpush.model.*;
import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

@Service
public class PushService {

    @Autowired
    private SubscriptionStore subscriptionStore;

    @Autowired
    private nl.martijndwars.webpush.PushService vapidPushClient;

    private ApnsClient safariPushClient;

    @Autowired
    private LogsStore logsStore;

    public PushService(ApnsClient client) {
        this.safariPushClient = client;
        try {
            this.safariPushClient.connect(ApnsClient.PRODUCTION_APNS_HOST).await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Push notification to the specific device.
     * @param subscription
     * @param notification
     */
    public void sendNotification(Subscription subscription, Notification notification) {
        String payload;
        if (subscription instanceof VapidSubscription) {
            payload = this.getPayload(notification, Notification.Type.VAPID);

        } else {
            payload = this.getPayload(notification, Notification.Type.SAFARI);
        }
        this.sendNotification(subscription, payload);
    }

    /**
     * Push notification to all devices
     * @param message
     */
    public void sendNotifications(Notification message) {
        String vapidPayload = this.getPayload(message, Notification.Type.VAPID);
        this.subscriptionStore.getVapidSubscriptions().forEach(subscription -> this.sendNotification(subscription, vapidPayload));

        String safariPayload = this.getPayload(message, Notification.Type.SAFARI);
        this.subscriptionStore.getSafariSubscriptions().forEach(subscription -> this.sendNotification(subscription, safariPayload));
    }




    private String getPayload (Notification notification, Notification.Type type) {
        if (type.equals(Notification.Type.VAPID)) {
            Map<String, Object> payload = new HashMap<>();
            Map<String, String> notificationContent = new HashMap<>();
            notificationContent.put("title", notification.getTitle());
            notificationContent.put("body", notification.getBody());
            notificationContent.put("icon", "./assets/icons/favicon.png");
            payload.put("notification", notificationContent);
            return new Gson().toJson(payload);
        }
        return new ApnsPayloadBuilder().setUrlArguments("").setAlertTitle(notification.getTitle()).setAlertBody(notification.getBody()).buildWithDefaultMaximumLength();
    }


    private void sendNotification(Subscription subscription, String payload) {
        if (subscription instanceof VapidSubscription) {
            VapidSubscription vapidSubscription = (VapidSubscription) subscription;
            try {
                nl.martijndwars.webpush.Notification toSend = new nl.martijndwars.webpush.Notification(vapidSubscription.getSubscription(), payload);
                this.vapidPushClient.send(toSend);
            } catch (Exception e) {
                //todo add logging
                throw new RuntimeException(e);
            }
            //todo add logging

        } else {

            SafariSubscription safariSubscription = (SafariSubscription) subscription;

            Future<PushNotificationResponse<SimpleApnsPushNotification>> sendNotificationFuture = this.safariPushClient.sendNotification(new SimpleApnsPushNotification(safariSubscription.getDeviceToken(), safariSubscription.getWebsitePushId(), payload));
            final PushNotificationResponse<SimpleApnsPushNotification> pushNotificationResponse;
            try {
                pushNotificationResponse = sendNotificationFuture.get();
                if (!pushNotificationResponse.isAccepted()) {
                    String result = "Notification rejected by the APNs gateway: " +
                            pushNotificationResponse.getRejectionReason();

                    if (pushNotificationResponse.getTokenInvalidationTimestamp() != null) {
                        result += "\t…and the token is invalid as of " +
                                pushNotificationResponse.getTokenInvalidationTimestamp();
                    }
                    Log safariLog = new Log();
                    safariLog.setLog(result);
                    this.logsStore.put(safariLog);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
