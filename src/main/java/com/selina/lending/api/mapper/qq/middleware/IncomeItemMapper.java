package com.selina.lending.api.mapper.qq.middleware;

import com.selina.lending.api.dto.common.IncomeItemDto;
import com.selina.lending.httpclient.middleware.dto.common.Income;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IncomeItemMapper {

    IncomeItemDto mapToIncomeItemDto(Income income);
    Income mapToIncome(IncomeItemDto incomeItemDto);

}
