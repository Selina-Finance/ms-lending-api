package com.selina.lending.api.dto.common;

public enum ApplicantTitle {
    MR("Mr."),
    MRS("Mrs."),
    MISS("Miss"),
    MS("Ms."),
    DR("Dr."),
    OTHER("Other");

    final String value;

    ApplicantTitle(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
