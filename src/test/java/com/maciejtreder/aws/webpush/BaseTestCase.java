package com.maciejtreder.aws.webpush;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.spring.SpringLambdaContainerHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.ConfigurableWebApplicationContext;

public class BaseTestCase {
//    private static final String AUTHORIZER_PRINCIPAL_ID = "test-principal-" + UUID.randomUUID().toString();


    @Autowired
    protected MockLambdaContext lambdaContext;

    @Autowired
    protected SpringLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    @Autowired
    private ConfigurableWebApplicationContext applicationContext;

}
