package by.javaguru.jdmk13.securityservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityRequestDto {
    private long requestId;
    private String type;
    private String name;
    private String surname;
}
