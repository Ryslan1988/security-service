package by.javaguru.jdmik12.securityservice.config;

import by.javaguru.jdmik12.common.base.KafkaMessage;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@EnableKafka
public class ProducerKafkaConfig {

    @Bean
    ProducerFactory<String, KafkaMessage> producerFactory(KafkaProperties kafkaProperties) {
        kafkaProperties.getProducer().setKeySerializer(StringSerializer.class);
        kafkaProperties.getProducer().setValueSerializer(JsonSerializer.class);
        var producerProperties = kafkaProperties.buildProducerProperties();

        return new DefaultKafkaProducerFactory<>(producerProperties);
    }

    @Bean
    KafkaTemplate<String, KafkaMessage> kafkaTemplate(ProducerFactory<String, KafkaMessage> producerFactory) {
        var kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setObservationEnabled(true);

        return kafkaTemplate;
    }

}

