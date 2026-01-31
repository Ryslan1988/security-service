package by.javaguru.jdmk13.securityservice.utils;

import by.javaguru.jdmk13.securityservice.model.SecurityResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class JsonInputProcess {
    private final String FILE_PATCH = "src/main/resources/data.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();

    public SecurityResponseDto jsonFileProcessUpdater(long id) throws IOException {
        String readString = Files.readString(Paths.get(FILE_PATCH));
        if (StringUtils.isBlank(readString)) {
            SecurityResponseDto securityResponseDto = new SecurityResponseDto(
                    random.nextInt(101), id, Math.random() < 0.5);
            objectMapper.writeValue(new File(FILE_PATCH), List.of(securityResponseDto));
            return securityResponseDto;
        }
        List<SecurityResponseDto> securityResponseDtoList = List.of(objectMapper.readValue(readString, SecurityResponseDto[].class));

        for (SecurityResponseDto responseDto : securityResponseDtoList) {
            if (responseDto.getRequestId() == id) {
                return responseDto;
            }
        }

        SecurityResponseDto securityResponseDto = new SecurityResponseDto(
                random.nextInt(101), id, Math.random() < 0.5);
        List<SecurityResponseDto> resList = new ArrayList<>(securityResponseDtoList);
        resList.add(securityResponseDto);

        objectMapper.writeValue(new File(FILE_PATCH), resList);

        return securityResponseDto;
    }
}
