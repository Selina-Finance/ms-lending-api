package com.selina.lending.api.mapper.qq.middleware;


import com.selina.lending.api.dto.common.IncomeDto;
import com.selina.lending.httpclient.middleware.dto.common.Incomes;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {IncomeItemMapper.class})
public interface IncomeMapper {

    Incomes mapToIcomes(IncomeDto incomeDto);
}

