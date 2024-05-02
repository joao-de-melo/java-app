package com.esg.rest.controller;

import com.esg.rest.model.CustomerDetails;
import com.esg.rest.persistence.CustomerDetailsRepository;
import com.esg.rest.persistence.model.CustomerDetailsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.StreamSupport;

@RestController
public class CustomerController {
    private final CustomerDetailsRepository repository;

    @Autowired
    public CustomerController(CustomerDetailsRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Boolean> post (@RequestBody CustomerDetails customerDetails) {
        repository.save(new CustomerDetailsEntity(
                customerDetails.customerRef(),
                customerDetails.customerName(),
                customerDetails.addressLine1(),
                customerDetails.addressLine2(),
                customerDetails.town(),
                customerDetails.county(),
                customerDetails.country(),
                customerDetails.postcode()
        ));
        return ResponseEntity.ok(true);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Long> list () {
        long count = StreamSupport.stream(
                repository.findAll().spliterator(),
                false
        ).count();

        return ResponseEntity.ok(count);
    }
}
