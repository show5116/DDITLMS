package com.example.dditlms.domain.repository.student;

import com.example.dditlms.domain.entity.SemesterByYear;
import com.example.dditlms.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long>,StudentRepositoryCustom {

    @Query(nativeQuery = true,value="SELECT * FROM STDNT s LEFT JOIN (SELECT * FROM REGIST r WHERE r.SEMSTR = :semester) re ON (s.MBER_NO = re.MBER_NO) WHERE re.REGIST_SN IS NULL AND s.STDNT_SE = 'ATTENDING'")
    public List<Student> getNotRegistedStudent(@Param("semester") String semester);
}
