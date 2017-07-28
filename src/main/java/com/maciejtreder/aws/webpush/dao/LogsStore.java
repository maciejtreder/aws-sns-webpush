package com.maciejtreder.aws.webpush.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.maciejtreder.aws.webpush.model.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class LogsStore {
    @Autowired
    private DynamoDBMapper mapper;

    public void put(Log log) {
        this.mapper.save(log);
    }

}
