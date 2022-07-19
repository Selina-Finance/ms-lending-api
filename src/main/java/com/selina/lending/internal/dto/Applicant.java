package com.selina.lending.internal.dto;

import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class Applicant {
    @NotBlank(message = "title is required")
    private String title;

    @Email(message = "emailAddress is not valid", regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    @NotBlank(message = "emailAddress is required")
    private String emailAddress;

    @NotBlank(message = "mobileNumber is required")
    private String mobileNumber;

    @NotBlank(message = "firstName is required")
    private String firstName;

    @NotBlank(message = "lastName is required")
    private String lastName;
    private String middleName;
    private String gender;

    @NotBlank(message = "dateOfBirth is required")
    private Date dateOfBirth;
    private int numberOfAdultDependants;
    private int numberOfChildDependants;

    @NotBlank(message = "livedInCurrrentAddressFor3Years is required")
    private Boolean livedInCurrentAddressFor3Years;
    private Boolean applicant2LivesWithApplicant1For3Years;
    private Boolean applicant2LivesWithApplicant1;
    private Date currentAddressMovedInDate;

    @NotNull(message = "address is required")
    private List<Address> addresses;
}
