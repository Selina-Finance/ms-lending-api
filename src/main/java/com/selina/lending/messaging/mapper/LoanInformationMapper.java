package com.selina.lending.messaging.mapper;

import com.selina.lending.internal.dto.LoanInformationDto;
import com.selina.lending.internal.service.application.domain.Facility;
import com.selina.lending.internal.service.application.domain.LoanInformation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class LoanInformationMapper {

    @Mapping(target = "facilities", source = "loanInformationDto", qualifiedByName = "FacilitiesMapper")
    public abstract LoanInformation mapLoanInformation(LoanInformationDto loanInformationDto);

    @Named("FacilitiesMapper")
    List<Facility> mapFacilities(LoanInformationDto loanInformation) {
        return List.of(Facility.builder()
                .allocationAmount(loanInformation.getRequestedLoanAmount())
                .allocationPurpose(loanInformation.getLoanPurpose())
                .build());
    }
}
