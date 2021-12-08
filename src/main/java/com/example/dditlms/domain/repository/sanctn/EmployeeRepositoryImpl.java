package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.dto.EmployeeDTO;
import com.example.dditlms.domain.dto.QEmployeeDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.dditlms.domain.entity.QDepartment.*;
import static com.example.dditlms.domain.entity.QEmployee.employee;

public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public EmployeeRepositoryImpl(EntityManager entityManager) {

        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public List<EmployeeDTO> viewDetails(Long userNumber) {

        return queryFactory
                .select(new QEmployeeDTO(employee.empSe
                        , department.deptNm))
                .from(employee)
                .from(department)
                .join(employee.deptCode, department)
                .where(employee.deptCode.eq(department)
                        , employee.userNumber.eq(userNumber))
                .fetch();

    }
}
