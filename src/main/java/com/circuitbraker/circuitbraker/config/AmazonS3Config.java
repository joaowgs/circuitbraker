package com.circuitbraker.circuitbraker.config;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AmazonS3Config {

    @Value("${aws.region}")
    private String awsRegion;
    @Value("${aws.access_key}")
    private String awsKey;
    @Value("${aws.secret_key}")
    private String awsSecret;

    @Bean
    public BasicAWSCredentials basicAWSCredentials (){
        return new BasicAWSCredentials(awsKey, awsSecret);
    }

    @Primary
    @Bean(name = "s3Configuration")
    public AmazonS3 amazonS3Client() {
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(awsRegion)
                .withPathStyleAccessEnabled(true)
                .build();
    }
}
