package by.javaguru.jdmk13.securityservice.service.impl;

import by.javaguru.jdmk13.securityservice.model.SecurityRequestDto;
import by.javaguru.jdmk13.securityservice.model.SecurityResponseDto;
import by.javaguru.jdmk13.securityservice.service.SecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public SecurityResponseDto getMockJsonById(SecurityRequestDto requestDto) throws IOException {
        return jsonFileUpdater(requestDto.getRequestId());

    }

    private SecurityResponseDto jsonFileUpdater(long id) throws IOException {
        Random random = new Random();
        String filePath = "src/main/resources/data.json";
        String readString = Files.readString(Paths.get(filePath));
        if (StringUtils.isBlank(readString)) {
            SecurityResponseDto accountingResponseDto = new SecurityResponseDto(
                    random.nextInt(101), id, Math.random() < 0.5);
            objectMapper.writeValue(new File(filePath), List.of(accountingResponseDto));
            return accountingResponseDto;
        }
        List<SecurityResponseDto> accountingResponseDtos = List.of(objectMapper.readValue(readString, SecurityResponseDto[].class));

        for (SecurityResponseDto responseDto : accountingResponseDtos) {
            if (responseDto.getRequestId() == id) {
                return responseDto;
            }
        }

        SecurityResponseDto accountingResponseDto = new SecurityResponseDto(
                random.nextInt(101), id, Math.random() < 0.5);
        List<SecurityResponseDto> resList = new ArrayList<>(accountingResponseDtos);
        resList.add(accountingResponseDto);

        objectMapper.writeValue(new File(filePath), resList);

        return accountingResponseDto;
    }
}
