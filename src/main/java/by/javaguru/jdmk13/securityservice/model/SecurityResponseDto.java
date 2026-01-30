package by.javaguru.jdmk13.securityservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityResponseDto {
    private long id;
    private long requestId;
    private boolean passed;
}
