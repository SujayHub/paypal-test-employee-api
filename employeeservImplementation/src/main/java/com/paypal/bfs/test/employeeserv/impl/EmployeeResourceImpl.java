package com.paypal.bfs.test.employeeserv.impl;

import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.entity.EmployeeEntity;
import com.paypal.bfs.test.employeeserv.mapper.impl.EmployeeMapperImpl;
import com.paypal.bfs.test.employeeserv.repo.EmployeeRepo;
import com.paypal.bfs.test.employeeserv.validator.AddressValidator;
import com.paypal.bfs.test.employeeserv.validator.EmployeeValidator;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/** Implementation class for employee resource. */
@RestController
public class EmployeeResourceImpl implements EmployeeResource {

  private static final Logger log = LoggerFactory.getLogger(EmployeeResourceImpl.class);
  private static final String EMPLOYEE_NOT_FOUND = "EMPLOYEE_NOT_FOUND";
  private static final String EMPLOYEE_ALREADY_EXISTS = "EMPLOYEE_ALREADY_EXISTS";

  private final EmployeeRepo employeeRepo;
  private final EmployeeMapperImpl employeeMapper;
  private final EmployeeValidator employeeValidator;
  private final AddressValidator addressValidator;

  @Autowired
  public EmployeeResourceImpl(
      final EmployeeRepo employeeRepo,
      final EmployeeMapperImpl employeeMapper,
      final EmployeeValidator employeeValidator,
      final AddressValidator addressValidator) {
    this.employeeRepo = employeeRepo;
    this.employeeMapper = employeeMapper;
    this.employeeValidator = employeeValidator;
    this.addressValidator = addressValidator;
  }

  @Override
  @SneakyThrows
  public ResponseEntity<Employee> employeeGetById(@NotNull @NotEmpty @NotBlank String id) {
    EmployeeEntity employeeEntity = employeeRepo.findById(Integer.valueOf(id)).orElse(null);
    if (Objects.nonNull(employeeEntity)) {
      log.info("returning success response");
      return new ResponseEntity<>(employeeMapper.toBo(employeeEntity), HttpStatus.OK);
    } else {
      log.info("employee by id={} is not found", id);
      Employee employee = new Employee();
      employee.setAdditionalProperty("message", EMPLOYEE_NOT_FOUND);
      return new ResponseEntity<>(employee, HttpStatus.NOT_FOUND);
    }
  }

  @Override
  @SneakyThrows
  public ResponseEntity<Employee> addNewEmployee(final Employee employee) {
    if (Objects.nonNull(employee.getAddress())) {
      log.info("validating address");
      this.addressValidator.validateAddress(employee.getAddress());
    }
    log.info("validating employee");
    employeeValidator.validateEmployee(employee);

    if (!employeeRepo.existsByEmail(employee.getEmail())) {
      log.info("adding new employee");
      EmployeeEntity employeeEntity = employeeRepo.save(employeeMapper.toEntity(employee));
      log.info("returning success response");
      return new ResponseEntity<>(employeeMapper.toBo(employeeEntity), HttpStatus.CREATED);
    } else {
      log.error("trying to add an employee who already exist");
      Employee duplicateEmployee = new Employee();
      duplicateEmployee.setAdditionalProperty("message", EMPLOYEE_ALREADY_EXISTS);
      return new ResponseEntity<>(duplicateEmployee, HttpStatus.CONFLICT);
    }
  }
}
