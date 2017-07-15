package com.maciejtreder.aws.webpush.model;

import lombok.Data;

@Data
public class NotificationWrapper {
    private String title;
    private String body;
    private String icon = "./assets/icons/favicon.png";
}