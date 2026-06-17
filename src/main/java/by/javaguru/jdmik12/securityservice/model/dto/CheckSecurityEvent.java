package by.javaguru.jdmik12.securityservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(setterPrefix = "with")
public record CheckSecurityEvent(
        Long id,
        Long requestId,
        Boolean isPassed
) implements KafkaMessage {
}
