package by.javaguru.jdmik12.securityservice.listener;

import by.javaguru.jdmik12.common.base.KafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityCheckProducerClient {

    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    public void sendMessage(String messageKey, String replayTopic, KafkaMessage message) {

        kafkaTemplate.send(replayTopic, messageKey, message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send SecurityCheck with key {}", messageKey, ex);
                    } else {
                        log.debug("Sent SecurityCheck with key {}", messageKey);
                    }
                });
    }
}

