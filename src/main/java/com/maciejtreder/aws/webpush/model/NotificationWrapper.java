package com.maciejtreder.aws.webpush.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationWrapper {
    private String title;
    private String body;
    @Builder.Default
    private String icon = "./assets/icons/favicon.png";
}