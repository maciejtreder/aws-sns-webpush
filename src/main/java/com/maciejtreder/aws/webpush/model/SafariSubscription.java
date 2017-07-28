package com.maciejtreder.aws.webpush.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@DynamoDBTable(tableName = "_safariSubscriptions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SafariSubscription implements Subscription {
    @DynamoDBHashKey
    private String deviceToken;
    @DynamoDBRangeKey
    private String websitePushId;
}
