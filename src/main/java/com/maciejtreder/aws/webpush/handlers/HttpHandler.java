package com.maciejtreder.aws.webpush.handlers;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.maciejtreder.aws.webpush.AwsWebPushApp;

public class HttpHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {

    SpringLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    {
        try {
            handler = SpringLambdaContainerHandler.getAwsProxyHandler(AwsWebPushApp.class);
        } catch (ContainerInitializationException e) {
            e.printStackTrace();
        }

    }

    public AwsProxyResponse handleRequest(AwsProxyRequest awsProxyRequest, Context context) {

        if(awsProxyRequest.getPath() == null) {//scheduler
            context.getLogger().log("warming up");
            return new AwsProxyResponse(200, null, "warmed");
        }

        awsProxyRequest.setPath("/" + awsProxyRequest.getPathParameters().get("proxy"));
        return handler.proxy(awsProxyRequest, context);
    }
}