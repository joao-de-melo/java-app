package com.esg.rest.controller;

import com.esg.rest.model.CustomerDetails;
import com.esg.rest.persistence.CustomerDetailsRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CustomerControllerTest {
    private final CustomerDetailsRepository customerDetailsRepository = mock(CustomerDetailsRepository.class);
    private final CustomerController underTest = new CustomerController(customerDetailsRepository);

    @Test
    void post() {
        CustomerDetails customer = new CustomerDetails("ref123",
                "John Doe", "123 Main St",
                "Apt 4B", "Anytown",
                "Anycounty", "Anycountry", "12345");

        ResponseEntity<Boolean> result = underTest.post(customer);

        verify(customerDetailsRepository).save(assertArg(entity -> assertThat(customer, Matchers.is(new CustomerDetails(
                entity.getCustomerRef(),
                entity.getCustomerName(),
                entity.getAddressLine1(),
                entity.getAddressLine2(),
                entity.getTown(),
                entity.getCounty(),
                entity.getCountry(),
                entity.getPostcode()
        )))));
        assertThat(result.getBody(), Matchers.is(true));
    }
}