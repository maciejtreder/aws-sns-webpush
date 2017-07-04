package com.maciejsobala.aws.webpush;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import nl.martijndwars.webpush.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public class SubscriptionStore {

    @Autowired
    private DynamoDBMapper mapper;

    public void put(Subscription subscription) {
        this.mapper.save(new SubscriptionWrapper(subscription));
    }

    public void delete(Subscription subscription) {
        this.mapper.delete(new SubscriptionWrapper(subscription));
    }

    public Stream<Subscription> getAll() {
//        new DynamoDBScanExpression().addFilterCondition();
        return this.mapper.scan(SubscriptionWrapper.class, new DynamoDBScanExpression()).parallelStream().map(subWrapper -> subWrapper.getSubscription());
    }
}
