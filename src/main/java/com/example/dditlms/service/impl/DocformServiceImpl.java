package com.example.dditlms.service.impl;

import com.example.dditlms.domain.dto.EmployeeDTO;
import com.example.dditlms.domain.entity.sanction.Docform;
import com.example.dditlms.domain.entity.sanction.EmployeeRole;
import com.example.dditlms.domain.repository.sanctn.DocformRepository;
import com.example.dditlms.domain.repository.sanctn.EmployeeRepository;
import com.example.dditlms.service.DocformService;
import com.example.dditlms.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DocformServiceImpl implements DocformService {

    private final DocformRepository docformRepository;

    private final EmployeeRepository employeeRepository;

    @Transactional
    @Override
    public void saveDocform(Docform docform) {
        docformRepository.save(docform);

    }

    @Override
    public String replaceDocform(String docformCn) {

        Long userNumber = MemberUtil.getLoginMember().getUserNumber();
        EmployeeDTO employeeDTO = employeeRepository.viewDetails(userNumber).get(0);
        String name = MemberUtil.getLoginMember().getName();
        String dept_nm = employeeDTO.getDept_nm();
        EmployeeRole employeeRole = employeeDTO.getEmployeeRole();
        String[] date = new LocalDate().toString().split("-");
        String s = date[0] + "년 ";
        String s1 = date[1] + "월 ";
        String s2 = date[2] + "일";
        String replace = docformCn.replace("${name}", name)
                .replace("${dept}", dept_nm)
                .replace("${role}", employeeRole.getKrName())
                .replace("${date}", s + s1 + s2);

        return replace;

    }


}
