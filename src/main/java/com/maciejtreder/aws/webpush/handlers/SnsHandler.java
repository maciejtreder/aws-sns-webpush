package com.maciejtreder.aws.webpush.handlers;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.maciejtreder.aws.webpush.SubscriptionWrapper;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.GeneralSecurityException;
import java.security.Security;

/*
TODO
refactor to reuse Spring app.
https://stackoverflow.com/questions/44995180/lambda-sns-spring-integration
 */
public class SnsHandler implements RequestHandler<SNSEvent, Object> {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    {
        DynamoDBMapperConfig.Builder builder = DynamoDBMapperConfig.builder();
        builder.setTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNamePrefix(System.getenv("TABLE_NAME")));
        this.dbMapper = new DynamoDBMapper(AmazonDynamoDBClientBuilder.defaultClient(), builder.build());
    }

    private String publicKey = System.getenv("PUBLIC_KEY");
    private String privateKey =  System.getenv("PRIVATE_KEY");

    private PushService pushService;
    private DynamoDBMapper dbMapper;


    public Object handleRequest(SNSEvent event, Context context) {
        this.sendNotifications(event.getRecords().get(0).getSNS().getMessage());

        return null;
    }

    private void sendNotifications(String payload) {
        if(pushService == null) {
            try {
                pushService = new PushService(this.publicKey, this.privateKey, "angular-universal-serverless by Maciej Treder <contact@maciejtreder.com>");
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
                return;
            }
        }
        dbMapper.scan(SubscriptionWrapper.class, new DynamoDBScanExpression()).parallelStream().map(wrapper -> wrapper.getSubscription()).forEach(sub -> {
            try {
                pushService.send(new Notification(sub, payload));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
