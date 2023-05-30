package com.selina.lending.internal.dto;

public enum SourceType {

    BROKER("Broker"),
    AGGREGATOR("Aggregator");

    final String value;

    SourceType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
