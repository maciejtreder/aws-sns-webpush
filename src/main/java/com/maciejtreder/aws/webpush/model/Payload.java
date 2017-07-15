package com.maciejtreder.aws.webpush.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Payload {
    private NotificationWrapper notification;
}
