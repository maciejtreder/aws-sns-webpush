package com.maciejtreder.aws.webpush.endpoints;

import com.maciejtreder.aws.webpush.PushService;
import com.maciejtreder.aws.webpush.dao.SubscriptionStore;
import com.maciejtreder.aws.webpush.model.Notification;
import com.maciejtreder.aws.webpush.model.VapidSubscription;
import nl.martijndwars.webpush.Subscription;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutionException;

@Profile("web")
@RestController
@EnableWebMvc
@RequestMapping("/vapid")
public class VapidSubscriptionEndpoint {

    @Autowired
    private SubscriptionStore store;

    @RequestMapping(path = "/subscribe", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String subscribe(@RequestBody Subscription subscription) {
        VapidSubscription vapidSubscription = new VapidSubscription(subscription);
        this.store.put(vapidSubscription);
        return "Subscription stored";
    }

    @RequestMapping(path = "/unsubscribe", method = RequestMethod.DELETE)
    public String unsubscribe(@RequestBody Subscription subscription) {
        VapidSubscription vapidSubscription = new VapidSubscription(subscription);
        this.store.delete(vapidSubscription);
        return "Subscription deleted";
    }

}
