package com.maciejtreder.aws.webpush.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.google.gson.Gson;
import lombok.Data;
import nl.martijndwars.webpush.Subscription;

@DynamoDBTable(tableName = "_subscriptions")
@Data
public class SubscriptionWrapper {

    @DynamoDBHashKey(attributeName = "subscription")
    private String key;
    @DynamoDBIgnore
    private Subscription subscription;

    public SubscriptionWrapper() {}

    public SubscriptionWrapper(Subscription sub) {
        this.key = new Gson().toJson(sub);
        this.subscription = sub;
    }

    public void setKey(String key) {
        this.key = key;
        this.subscription = new Gson().fromJson(key, Subscription.class);
    }
}
