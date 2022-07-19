package com.selina.lending.internal.service.application.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Employment {

    private String employmentStatus;
    private Boolean inProbationPeriod;
    private String employmentType;
    private String employerPhoneNumber;
    private String postcode;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private Date contractStartDate;
    private Date contractEndDate;
    private Boolean firstTimeContractor;
    private String employerName;
    private String jobTitle;
    private String occupationType;
    private Boolean isEmployedByFamilyMember;
    private Boolean ownSharesInThisCompany;
    private Double shareHolding;
    private String registeredCompanyName;
    private String selfEmployed;
    private String fiscalYearReportedIncomeRelatesTo;
    private String lengthSelfEmployed;
    private Integer companyRegistrationNumber;
    private Integer percentageOfCompanyOwned;
    private String monthAccountingPeriodStarts;
    private Date whenWasCompanyIncorporated;
    private Date whenDidYouBeginTrading;
    private Date startDate;
    private Date partnershipFormedDate;
    private Double percentageOfPartnershipOwned;
    private String businessStructure;
    private String industry;
}
