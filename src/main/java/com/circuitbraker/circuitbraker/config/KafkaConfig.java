package com.circuitbraker.circuitbraker.config;

import com.circuitbraker.circuitbraker.config.props.KafkaProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.LoggingErrorHandler;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@Profile(value = "dev")
public class KafkaConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory jsonKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(jsonConsumerFactory());
        factory.setMessageConverter(new JsonMessageConverter());
        factory.setErrorHandler(new LoggingErrorHandler());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, Object> jsonConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerJsonProps());
    }

    protected Map<String, Object> consumerJsonProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(KafkaProperties.ENABLE_AUTO_COMMIT, Boolean.TRUE.toString());
        props.put(KafkaProperties.AUTO_COMMIT_INTERVAL, "1000");
        props.put(KafkaProperties.MAX_POLL_RECORDS, 500);
        return props;
    }
}
