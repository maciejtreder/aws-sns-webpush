package com.maciejtreder.aws.webpush.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.maciejtreder.aws.webpush.model.SafariSubscription;
import com.maciejtreder.aws.webpush.model.Subscription;
import com.maciejtreder.aws.webpush.model.VapidSubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public class SubscriptionStore {
    @Autowired
    private DynamoDBMapper mapper;

    private static final DynamoDBScanExpression emptyScan = new DynamoDBScanExpression();

    public void put(Subscription subscription) {
        this.mapper.save(subscription);
    }

    public void delete(Subscription subscription) {
        this.mapper.delete(subscription);
    }

    public Stream<Subscription> getAll() {
        return Stream.concat(this.mapper.scan(VapidSubscription.class, emptyScan).parallelStream(), this.mapper.scan(SafariSubscription.class, emptyScan).parallelStream());
    }

    public Stream<VapidSubscription> getVapidSubscriptions() {
        return this.mapper.scan(VapidSubscription.class, emptyScan).parallelStream();
    }

    public Stream<SafariSubscription> getSafariSubscriptions() {
        return this.mapper.scan(SafariSubscription.class, emptyScan).parallelStream();
    }
}
