package com.selina.lending.api.dto.common;

public enum ApplicantResidentialStatus {
    OWNER("Owner"),
    OWNER_OCCUPIER("Owner Occupier");
    final String value;

    ApplicantResidentialStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
