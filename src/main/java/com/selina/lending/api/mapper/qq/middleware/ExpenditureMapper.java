package com.selina.lending.api.mapper.qq.middleware;

import com.selina.lending.api.dto.common.ExpenditureDto;
import com.selina.lending.httpclient.middleware.dto.common.Expenditure;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.util.StringUtils;

@Slf4j
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ExpenditureMapper {

    private static final String DEFAULT_EXPENDITURE_FREQUENCY = "monthly";

    @Mapping(target = "frequency", source = "expenditureDto.frequency", qualifiedByName = "mapExpenditureFrequency")
    public abstract Expenditure mapToExpenditure(ExpenditureDto expenditureDto);

    @Named("mapExpenditureFrequency")
    String mapExpenditureFrequency(String frequency) {
        String mappedFrequency = frequency;

        if (!StringUtils.hasText(mappedFrequency)) {
            mappedFrequency = DEFAULT_EXPENDITURE_FREQUENCY;
            log.warn("Expenditure frequency is not specified. Defaulting to 'monthly'.");
        }

        return mappedFrequency;
    }
}
