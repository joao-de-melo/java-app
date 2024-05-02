package com.esg.rest.persistence.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_details")
public class CustomerDetailsEntity {
    @Id
    private String customerRef;
    private String customerName;
    private String addressLine1;
    private String addressLine2;
    private String town;
    private String county;
    private String country;
    private String postcode;

    // Constructor
    public CustomerDetailsEntity(String customerRef, String customerName, String addressLine1,
                           String addressLine2, String town, String county,
                           String country, String postcode) {
        this.customerRef = customerRef;
        this.customerName = customerName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.town = town;
        this.county = county;
        this.country = country;
        this.postcode = postcode;
    }

    public CustomerDetailsEntity() {
    }

    // Getters
    public String getCustomerRef() {
        return customerRef;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getTown() {
        return town;
    }

    public String getCounty() {
        return county;
    }

    public String getCountry() {
        return country;
    }

    public String getPostcode() {
        return postcode;
    }
}
