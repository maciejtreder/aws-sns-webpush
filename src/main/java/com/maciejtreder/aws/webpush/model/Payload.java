package com.maciejtreder.aws.webpush.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Payload {
    private NotificationWrapper notification;
}
