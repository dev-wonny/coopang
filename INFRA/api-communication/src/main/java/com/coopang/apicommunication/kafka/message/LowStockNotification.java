package com.coopang.apicommunication.kafka.message;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LowStockNotification {
    private UUID productId;
    private int quantity;
    private String slackId;

}
