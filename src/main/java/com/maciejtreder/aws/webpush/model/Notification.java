package com.maciejtreder.aws.webpush.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Notification {
    private String title;
    private String body;

    public enum Type {
        VAPID, SAFARI
    }
}
