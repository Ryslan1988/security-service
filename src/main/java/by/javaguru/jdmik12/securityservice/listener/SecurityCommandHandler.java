package by.javaguru.jdmik12.securityservice.listener;

import by.javaguru.jdmik12.common.security.message.command.CheckSecurityCommand;
import by.javaguru.jdmik12.common.security.message.event.CheckSecurityEvent;
import by.javaguru.jdmik12.securityservice.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = "${integration.kafka.consumer.security.topic.name}", containerFactory = "kafkaListenerContainerFactory")
public class SecurityCommandHandler {
    private final SecurityService securityService;
    private final SecurityCheckProducerClient securityCheckProducerClient;

    @KafkaHandler
    public void handleAllocatedCommand(
            @Payload CheckSecurityCommand securityCommand,
            @Header(KafkaHeaders.RECEIVED_KEY) String messageKey,
            @Header(KafkaHeaders.REPLY_TOPIC) String replayTopic) throws IOException {

        log.debug("Receiver checkSecurity with key {} {}", messageKey, securityCommand);
        CheckSecurityEvent serviceMockJsonById = securityService.getMockJsonById(securityCommand.requestId());
        log.debug("Processed checkSecurity with key {}", messageKey);

        securityCheckProducerClient.sendMessage(messageKey, replayTopic, serviceMockJsonById);
    }
}

