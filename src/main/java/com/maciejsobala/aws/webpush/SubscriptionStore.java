package com.maciejsobala.aws.webpush;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import nl.martijndwars.webpush.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Repository
public class SubscriptionStore {

    @Autowired
    private DynamoDBMapper mapper;

    public void put(Subscription subscription) {
        SubscriptionWrapper sw = new SubscriptionWrapper(subscription);
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(sw.getKey()));
        int rowCount = this.mapper.count(SubscriptionWrapper.class, new DynamoDBScanExpression().withFilterExpression("subscription = :val1"));
        System.out.println(rowCount);
        this.mapper.save(sw);
    }

    public void delete(Subscription subscription) {
        this.mapper.delete(new SubscriptionWrapper(subscription));
    }

    public Stream<Subscription> getAll() {
//        new DynamoDBScanExpression().addFilterCondition();
        return this.mapper.scan(SubscriptionWrapper.class, new DynamoDBScanExpression()).parallelStream().map(subWrapper -> subWrapper.getSubscription());
    }
}
