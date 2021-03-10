package com.paypal.bfs.test.employeeserv.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;

import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.entity.EmployeeEntity;
import com.paypal.bfs.test.employeeserv.mapper.impl.EmployeeMapperImpl;
import com.paypal.bfs.test.employeeserv.repo.EmployeeRepo;
import com.paypal.bfs.test.employeeserv.validator.AddressValidator;
import com.paypal.bfs.test.employeeserv.validator.EmployeeValidator;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class EmployeeResourceImplTest {

  @Mock private EmployeeRepo employeeRepo;

  @Mock private EmployeeMapperImpl employeeMapper;

  @Mock private AddressValidator addressValidator;

  @Mock private EmployeeValidator employeeValidator;

  @InjectMocks private EmployeeResourceImpl employeeResource;

  @Test
  void smokeTest() {
    assertNotNull(employeeResource);
    assertNotNull(employeeRepo);
    assertNotNull(employeeMapper);
  }

  @Test
  void existingEmployeeIdShouldReturnValidEmployee() {
    doReturn(Optional.of(getDefaultEmployeeEntity())).when(employeeRepo).findById(anyInt());
    doReturn(getDefaultEmployee()).when(employeeMapper).toBo(any());
    ResponseEntity<Employee> employeeResponseEntity = employeeResource.employeeGetById("1");
    assertEquals(HttpStatus.OK, employeeResponseEntity.getStatusCode());
    assertNotNull(employeeResponseEntity.getBody());
    assertEquals(Integer.valueOf(1), employeeResponseEntity.getBody().getId());
    assertEquals("John", employeeResponseEntity.getBody().getFirstName());
    assertEquals("Doe", employeeResponseEntity.getBody().getLastName());
    assertEquals("03-12-1994", employeeResponseEntity.getBody().getDateOfBirth());
    assertEquals("john.doe@example.com", employeeResponseEntity.getBody().getEmail());
  }

  @Test
  void NonExistingEmployeeIdShouldReturnNotFoundStatus() {
    doReturn(Optional.empty()).when(employeeRepo).findById(anyInt());
    doReturn(null).when(employeeMapper).toBo(any());
    ResponseEntity<Employee> employeeResponseEntity = employeeResource.employeeGetById("1");
    assertEquals(HttpStatus.NOT_FOUND, employeeResponseEntity.getStatusCode());
  }

  @Test
  void addingNewEmployeeShouldReturnOkStatus() {
    doReturn(getDefaultEmployeeEntity()).when(employeeRepo).save(any());
    doReturn(getDefaultEmployeeEntity()).when(employeeMapper).toEntity(any());
    doReturn(getDefaultEmployee()).when(employeeMapper).toBo(any());
    ResponseEntity<Employee> employeeResponseEntity =
        employeeResource.addNewEmployee(getDefaultEmployee());
    assertEquals(HttpStatus.CREATED, employeeResponseEntity.getStatusCode());
    assertNotNull(employeeResponseEntity.getBody());
    assertEquals(Integer.valueOf(1), employeeResponseEntity.getBody().getId());
    assertEquals("John", employeeResponseEntity.getBody().getFirstName());
    assertEquals("Doe", employeeResponseEntity.getBody().getLastName());
    assertEquals("03-12-1994", employeeResponseEntity.getBody().getDateOfBirth());
    assertEquals("john.doe@example.com", employeeResponseEntity.getBody().getEmail());
  }

  @Test
  void whenEmployeeFirstNameIsMissingAddingNewemployeeShouldReturnBadRequest() {
    doReturn(getDefaultEmployeeEntity()).when(employeeRepo).save(any());
    doReturn(getDefaultEmployeeEntity()).when(employeeMapper).toEntity(any());
    doReturn(getDefaultEmployee()).when(employeeMapper).toBo(any());
    ResponseEntity<Employee> employeeResponseEntity =
        employeeResource.addNewEmployee(getDefaultEmployee());
    assertEquals(HttpStatus.CREATED, employeeResponseEntity.getStatusCode());
    assertNotNull(employeeResponseEntity.getBody());
    assertEquals(Integer.valueOf(1), employeeResponseEntity.getBody().getId());
    assertEquals("John", employeeResponseEntity.getBody().getFirstName());
    assertEquals("Doe", employeeResponseEntity.getBody().getLastName());
    assertEquals("03-12-1994", employeeResponseEntity.getBody().getDateOfBirth());
    assertEquals("john.doe@example.com", employeeResponseEntity.getBody().getEmail());
  }

  @Test
  void addingDuplicateEmployeeShouldReturnConflictStatus() {
    doReturn(true).when(employeeRepo).existsByEmail(any());
    ResponseEntity<Employee> employeeResponseEntity =
        employeeResource.addNewEmployee(getDefaultEmployee());
    assertEquals(HttpStatus.CONFLICT, employeeResponseEntity.getStatusCode());
    assertNotNull(employeeResponseEntity.getBody());
  }

  private EmployeeEntity getDefaultEmployeeEntity() {
    EmployeeMapperImpl employeeMapper = new EmployeeMapperImpl();
    EmployeeEntity employeeEntity = employeeMapper.toEntity(getDefaultEmployee());
    employeeEntity.setId(1);
    return employeeEntity;
  }

  private Employee getDefaultEmployee() {
    Employee employee = new Employee();
    Address address = new Address();
    address.setLine1("147 Lancaster St");
    address.setLine2("Brooklyn");
    address.setCity("NY");
    address.setState("NY");
    address.setCountry("USA");
    address.setZipCode(112301);
    employee.setId(1);
    employee.setFirstName("John");
    employee.setLastName("Doe");
    employee.setDateOfBirth("03-12-1994");
    employee.setEmail("john.doe@example.com");
    employee.setAddress(address);
    return employee;
  }
}
