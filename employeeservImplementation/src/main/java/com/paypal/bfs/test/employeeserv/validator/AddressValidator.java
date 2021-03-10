package com.paypal.bfs.test.employeeserv.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.impl.EmployeeResourceImpl;
import java.util.Objects;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaClient;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Component;

@Component
public class AddressValidator {

  public void validateAddress(Address address) throws JsonProcessingException {

    ObjectMapper objectMapper = new ObjectMapper();
    JSONObject jsonSchema =
        new JSONObject(
            new JSONTokener(
                Objects.requireNonNull(
                    EmployeeResourceImpl.class.getResourceAsStream("/v1/schema/address.json"))));
    JSONObject jsonSubject =
        new JSONObject(new JSONTokener(objectMapper.writeValueAsString(address)));
    Schema schemaLoader =
        SchemaLoader.builder()
            .schemaClient(SchemaClient.classPathAwareClient())
            .schemaJson(jsonSchema)
            .resolutionScope("classpath://v1/schema/") // setting the default resolution scope
            .build()
            .load()
            .build();
    schemaLoader.validate(jsonSubject);
  }
}
