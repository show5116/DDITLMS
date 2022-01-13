package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.entity.Employee;
import com.example.dditlms.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, EmployeeRepositoryCustom {
    Optional<Employee> findByMember(Member member);
}

