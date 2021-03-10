package com.paypal.bfs.test.employeeserv.mapper.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.entity.EmployeeEntity;
import com.paypal.bfs.test.employeeserv.mapper.EntityBoMapper;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapperImpl implements EntityBoMapper<EmployeeEntity, Employee> {

  private static final Logger log = LoggerFactory.getLogger(EmployeeMapperImpl.class);

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public EmployeeEntity toEntity(final Employee employee) {
    if (Objects.isNull(employee)) {
      return null;
    }
    log.info("converting employee to employeeEntity");
    EmployeeEntity employeeEntity =
        EmployeeEntity.builder()
            .firstName(employee.getFirstName())
            .lastName(employee.getLastName())
            .email(employee.getEmail())
            .dateOfBirth(fromStringToDate(employee.getDateOfBirth()))
            .address(fromAddressToJsonString(employee.getAddress()))
            .build();
    log.info("successfully converted employee to employeeEntity");
    return employeeEntity;
  }

  private String fromAddressToJsonString(final Address address) {
    try {
      return this.objectMapper.writeValueAsString(address);
    } catch (JsonProcessingException e) {
      log.error("unable to process json object ", e);
      throw new RuntimeException("JSON_PROCESSING_FAILURE");
    }
  }

  private Date fromStringToDate(final String dateOfBirth) {

    if (Objects.isNull(dateOfBirth)) {
      return null;
    }
    String pattern = "dd-MM-yyyy";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    try {
      return simpleDateFormat.parse(dateOfBirth);
    } catch (ParseException e) {
      log.error("unable to parse date string to format dd-MM-yyyy ", e);
      throw new RuntimeException("DATE_PROCESSING_FAILURE");
    }
  }

  @Override
  public Employee toBo(EmployeeEntity employeeEntity) {
    if (Objects.isNull(employeeEntity)) {
      return null;
    }
    log.info("converting employee entity to employee");
    Employee employee = new Employee();
    employee.setId(employeeEntity.getId());
    employee.setFirstName(employeeEntity.getFirstName());
    employee.setLastName(employeeEntity.getLastName());
    employee.setEmail(employeeEntity.getEmail());
    employee.setDateOfBirth(fromDateToString(employeeEntity.getDateOfBirth()));
    employee.setAddress(fromJsonStringToAddress(employeeEntity.getAddress()));
    log.info("successfully converted employee entity to employee");
    return employee;
  }

  private Address fromJsonStringToAddress(final String address) {
    try {
      return this.objectMapper.readValue(address, Address.class);
    } catch (IOException e) {
      log.error("unable to process json string ", e);
      throw new RuntimeException("JSON_PROCESSING_FAILURE");
    }
  }

  private String fromDateToString(final Date dateOfBirth) {
    if (Objects.isNull(dateOfBirth)) {
      return null;
    }
    String pattern = "dd-MM-yyyy";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    return simpleDateFormat.format(dateOfBirth);
  }
}
