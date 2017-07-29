package com.maciejtreder.aws.webpush.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Notification {
    private String title;
    private String body;
    private String url=""; //redirect url, needs logic on the client (front-end) side.

    public Notification(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public enum Type {
        VAPID, SAFARI
    }
}
