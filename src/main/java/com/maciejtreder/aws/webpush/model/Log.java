package com.maciejtreder.aws.webpush.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@DynamoDBTable(tableName = "_logs")
@Data
public class Log {
    @DynamoDBHashKey
    private Date timestamp = new Date();

    @DynamoDBAttribute
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.SS)
    private Set<String> logs;

    public void setLog(String log) {
        if (this.logs == null) {
            this.logs = new HashSet<>();
        }
        this.logs.add(log);
    }
}
