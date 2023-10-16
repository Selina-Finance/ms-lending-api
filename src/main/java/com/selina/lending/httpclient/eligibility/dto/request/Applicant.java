package com.selina.lending.httpclient.eligibility.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
public class Applicant {

    List<Income> incomes;
}
