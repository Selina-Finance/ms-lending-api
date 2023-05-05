package com.selina.lending.internal.dto;

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
