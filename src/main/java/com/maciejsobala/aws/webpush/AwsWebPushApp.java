package com.maciejsobala.aws.webpush;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import nl.martijndwars.webpush.PushService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.security.GeneralSecurityException;
import java.security.Security;

@Configuration
@ComponentScan("com.maciejsobala.aws.webpush")
public class AwsWebPushApp {

    @Value("${PRIVATE_KEY}")
    private String privateKey;

    @Value("${PUBLIC_KEY}")
    private String publicKey;

    @Bean
    public PushService pushService() throws GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        return new PushService(this.publicKey, this.privateKey, "mailto:contact@maciejtreder.com");
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
}
