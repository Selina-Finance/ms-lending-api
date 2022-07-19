package com.selina.lending.internal.service.application.dto;

import java.util.Date;
import java.util.List;
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
public class Applicant {
    private String title;
    private String emailAddress;
    private String mobileNumber;
    private String firstName;
    private String lastName;
    private String middleName;
    private String gender;
    private Date dateOfBirth;
    private int numberOfAdultDependants;
    private int numberOfChildDependants;
    private Boolean livedInCurrentAddressFor3Years;
    private Boolean applicant2LivesWithApplicant1For3Years;
    private Boolean applicant2LivesWithApplicant1;
    private Date currentAddressMovedInDate;
    private List<Address> addresses;
}
