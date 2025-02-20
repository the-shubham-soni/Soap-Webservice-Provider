package com.springbootsoap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springbootsoap.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{


	Employee findByEmployeeId(long employeeId);

}
