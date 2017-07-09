package com.maciejtreder.aws.webpush;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import nl.martijndwars.webpush.PushService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;

@Configuration
@ComponentScan("com.maciejtreder.aws.webpush")
public class AwsWebPushApp {

    @Value("${VAPID_PRIVATE_KEY}")
    private String vapidPrivateKey;

    @Value("${VAPID_PUBLIC_KEY}")
    private String vapidPublicKey;

    @Value("${SAFARI_KEY_PASSWORD}")
    private String safariKeyPassword;

    @Bean
    public PushService vapidPushService() throws GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        return new PushService(this.vapidPublicKey, this.vapidPrivateKey, "mailto:contact@maciejtreder.com");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:8000", "https://www.angular-universal-serverless.maciejtreder.com");
            }
        };
    }

    @Bean
    public DynamoDBMapper mapper(DynamoDBMapperConfig config) {
        return new DynamoDBMapper(AmazonDynamoDBClientBuilder.defaultClient(), config);
    }

    @Bean
    public DynamoDBMapperConfig dynamoDBMapperConfig() {
        DynamoDBMapperConfig.Builder builder = DynamoDBMapperConfig.builder();
        builder.setTableNameOverride(DynamoDBMapperConfig.TableNameOverride.withTableNamePrefix(System.getenv("TABLE_NAME")));
        return builder.build();
    }

    @Bean
    public ApnsClient apnsClient() throws IOException {
        return new ApnsClientBuilder().setClientCredentials(new File(getClass().getClassLoader().getResource("cert.p12").getFile()), this.safariKeyPassword).build();
    }

}
