package com.paypal.bfs.test.employeeserv.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Entity
public class EmployeeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Integer id;

  private String firstName;

  private String lastName;

  @Basic
  @Temporal(TemporalType.DATE)
  @DateTimeFormat(pattern = "dd-MM-yyyy")
  private Date dateOfBirth;

  private String email;

  @Column(columnDefinition = "clob")
  private String address;

  public EmployeeEntity() {

  }

  @Builder
  public EmployeeEntity(
      final String firstName,
      final String lastName,
      final Date dateOfBirth,
      final String email,
      final String address) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.email = email;
    this.address = address;
  }
}
