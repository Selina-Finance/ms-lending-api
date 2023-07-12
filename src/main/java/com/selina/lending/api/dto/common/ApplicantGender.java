package com.selina.lending.api.dto.common;

public enum ApplicantGender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    final String value;

    ApplicantGender(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
