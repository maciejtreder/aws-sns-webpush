package com.maciejsobala.aws.webpush;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.google.gson.Gson;
import nl.martijndwars.webpush.Subscription;

@DynamoDBTable(tableName = "_subscriptions")
public class SubscriptionWrapper {


    private String subscription;

    public SubscriptionWrapper() {}

    public SubscriptionWrapper(Subscription sub) {
        this.subscription = new Gson().toJson(sub);
    }

    @DynamoDBIgnore
    public Subscription getSubscription() {
        return new Gson().fromJson(this.subscription, Subscription.class);
    }

    @DynamoDBHashKey(attributeName = "subscription")
    public String getKey() {
        return this.subscription;
    }
    public void setKey(String key) {
        this.subscription = key;
    }
}
