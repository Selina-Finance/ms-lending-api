package com.selina.lending.api.mapper.qq.middleware;

import com.selina.lending.api.dto.common.ExpenditureDto;
import com.selina.lending.httpclient.middleware.dto.common.Expenditure;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExpenditureMapper {
    Expenditure mapToExpenditure(ExpenditureDto expenditureDto);
}
