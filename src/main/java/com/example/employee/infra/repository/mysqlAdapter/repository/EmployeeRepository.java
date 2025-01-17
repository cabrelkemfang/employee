package com.example.employee.infra.repository.mysqlAdapter.repository;

import com.example.employee.infra.repository.mysqlAdapter.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

//    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM Employee u WHERE u.email = :email")
    boolean existsByEmail(String email);
}
