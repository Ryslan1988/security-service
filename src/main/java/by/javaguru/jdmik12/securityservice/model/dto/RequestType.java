package by.javaguru.jdmik12.securityservice.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RequestType {
    ONBOARDING("onboarding"),

    RECRUITMENT_VN("recruitment-vn"),

    RECRUITMENT_PR("recruitment-pr"),

    RESIGNATION("resignation"),

    RELOCATION("relocation"),

    VACATION("vacation"),

    EDUCATION("education"),

    NOTIFICATION("notification");

    private final String value;

    RequestType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static RequestType fromValue(String value) {
        for (RequestType b : RequestType.values()) {
            if (b.value.equalsIgnoreCase(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
