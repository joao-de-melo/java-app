package com.esg.console.model;

public record CustomerDetails(String customerRef, String customerName, String addressLine1, String addressLine2,
                              String town, String county, String country, String postcode) {
}
