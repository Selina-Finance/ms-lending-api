package com.selina.lending.internal.mapper.quote.middleware;

import com.selina.lending.internal.dto.IncomeItemDto;
import com.selina.lending.internal.service.application.domain.Income;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IncomeItemMapper {

    IncomeItemDto mapToIncomeItemDto(Income income);
    Income mapToIncome(IncomeItemDto incomeItemDto);

}