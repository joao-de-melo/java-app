package com.esg.rest.persistence;

import com.esg.rest.persistence.model.CustomerDetailsEntity;
import org.springframework.data.repository.CrudRepository;

public interface CustomerDetailsRepository extends CrudRepository<CustomerDetailsEntity, String> {

}
