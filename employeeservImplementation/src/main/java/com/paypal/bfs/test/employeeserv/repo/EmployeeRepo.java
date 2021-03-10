package com.paypal.bfs.test.employeeserv.repo;

import com.paypal.bfs.test.employeeserv.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends JpaRepository<EmployeeEntity, Integer> {

  boolean existsByEmail(final String email);
}
