package com.maciejtreder.aws.webpush.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.google.gson.Gson;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.martijndwars.webpush.Subscription;

@DynamoDBTable(tableName = "_vapidSubscriptions")
@Data
@NoArgsConstructor
public class VapidSubscription implements com.maciejtreder.aws.webpush.model.Subscription {

    @DynamoDBHashKey(attributeName = "subscription")
    private String key;
    @DynamoDBIgnore
    private Subscription subscription;

    public VapidSubscription(Subscription sub) {
        this.key = new Gson().toJson(sub);
        this.subscription = sub;
    }

    public void setKey(String key) {
        this.key = key;
        this.subscription = new Gson().fromJson(key, Subscription.class);
    }
}
