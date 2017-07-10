package com.maciejsobala.aws.webpush;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import org.apache.http.HttpResponse;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutionException;

@RestController
@EnableWebMvc
public class SubscriptionEndpoint {

    @Autowired
    private SubscriptionStore store;

    @Autowired
    private PushService pushService;

    @RequestMapping(path = "/subscribe", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String subscribe(@RequestBody Subscription subscription) throws GeneralSecurityException, InterruptedException, JoseException, ExecutionException, IOException {
        HttpResponse resp = this.pushService.send(new Notification(subscription, "Don't forget to star this repo on github!"));
        this.store.put(subscription);
        return "Subscription stored";
    }

    @RequestMapping(path = "/unsubscribe", method = RequestMethod.DELETE)
    public String unsubscribe(@RequestBody Subscription subscription) {
        this.store.delete(subscription);
        return "Subscription deleted";
    }

}
