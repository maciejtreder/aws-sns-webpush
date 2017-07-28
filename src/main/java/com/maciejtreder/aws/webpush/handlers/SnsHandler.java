package com.maciejtreder.aws.webpush.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.maciejtreder.aws.webpush.AwsWebPushApp;
import com.maciejtreder.aws.webpush.PushService;
import com.maciejtreder.aws.webpush.model.Notification;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;


public class SnsHandler implements RequestHandler<SNSEvent, Object> {

    private AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
    private PushService pushService;

    {
        this.applicationContext.register(AwsWebPushApp.class);
        this.applicationContext.refresh();
        this.pushService = this.applicationContext.getBean(PushService.class);
    }


    public Object handleRequest(SNSEvent event, Context context) {
        SNSEvent.SNS sns = event.getRecords().get(0).getSNS();
        this.pushService.sendNotifications(new Notification(sns.getSubject(), sns.getMessage()));
        return null;
    }

}
