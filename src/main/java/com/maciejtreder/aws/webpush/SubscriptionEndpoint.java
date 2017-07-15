package com.maciejtreder.aws.webpush;

import com.google.gson.Gson;
import com.maciejtreder.aws.webpush.model.NotificationWrapper;
import com.maciejtreder.aws.webpush.model.Payload;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
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
        Payload payload = new Payload(NotificationWrapper.builder().title("Star on GitHub").body("Don't forget to star this repo on GitHub!").build());
        this.pushService.send(new Notification(subscription, new Gson().toJson(payload)));
        this.store.put(subscription);
        return "Subscription stored";
    }

    @RequestMapping(path = "/unsubscribe", method = RequestMethod.DELETE)
    public String unsubscribe(@RequestBody Subscription subscription) {
        this.store.delete(subscription);
        return "Subscription deleted";
    }

}
