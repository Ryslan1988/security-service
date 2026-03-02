package by.javaguru.jdmik12.securityservice.config;

import by.javaguru.jdmik12.common.base.KafkaMessage;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.validation.ValidationException;
import org.apache.commons.lang3.SerializationException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.nio.file.AccessDeniedException;

@Configuration
public class ConsumerKafkaConfig {
    @Value("${integration.kafka.producer.dlt.topic.name}")
    private String dltTopic;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            KafkaProperties kafkaProperties,
            ConsumerFactory<String, Object> consumerFactory,
            DefaultErrorHandler errorHandler) {

        var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory(KafkaProperties kafkaProperties) {
        var consumerProps = kafkaProperties.buildConsumerProperties();

        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                ErrorHandlingDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                ErrorHandlingDeserializer.class);

        consumerProps.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS,
                StringDeserializer.class);
        consumerProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS,
                JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(consumerProps);
    }

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, KafkaMessage> kafkaTemplate) {
        FixedBackOff backOff = new FixedBackOff(1000L, 3);

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                kafkaTemplate,
                (record, exception) -> new TopicPartition(dltTopic, record.partition()));

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, backOff);

        errorHandler.addNotRetryableExceptions(IllegalArgumentException.class, IllegalStateException.class,
                ValidationException.class, JsonParseException.class, JsonMappingException.class, SerializationException.class,
                AccessDeniedException.class);

        return errorHandler;
    }

}
