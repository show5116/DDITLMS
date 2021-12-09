package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.dto.EmployeeDTO;
import com.example.dditlms.domain.dto.QEmployeeDTO;
import com.example.dditlms.domain.entity.QDepartment;
import com.example.dditlms.domain.entity.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.dditlms.domain.entity.QDepartment.*;
import static com.example.dditlms.domain.entity.QEmployee.employee;
import static com.example.dditlms.domain.entity.QMember.member;

public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public EmployeeRepositoryImpl(EntityManager entityManager) {

        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public List<EmployeeDTO> viewDetails(Long userNumber) {

        return queryFactory
                .select(new QEmployeeDTO(employee.employeeRole
                        , department.deptNm))
                .from(employee)
                .from(department)
                .join(employee.deptCode, department)
                .where(employee.deptCode.eq(department)
                        , employee.userNumber.eq(userNumber))
                .fetch();

    }

    @Override
    public List<EmployeeDTO> empList(Long depCode) {

        return queryFactory
                .select(new QEmployeeDTO(employee.employeeRole, member.name, employee.userNumber))
                .from(employee)
                .from(department)
                .from(member)
                .join(employee.deptCode, department)
                .join(employee.member, member)
                .where(employee.deptCode.eq(department)
                , department.departmentCode.eq(depCode), employee.userNumber.eq(member.userNumber))
                .groupBy(employee)
                .fetch();

    }

}
