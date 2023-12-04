package com.selina.lending.service;

public enum SourceType {

    BROKER("Broker"),
    AGGREGATOR("Aggregator"),
    DIRECT("Direct");

    final String value;

    SourceType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
