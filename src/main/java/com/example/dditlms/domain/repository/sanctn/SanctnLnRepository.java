package com.example.dditlms.domain.repository.sanctn;

import com.example.dditlms.domain.entity.sanction.SanctnLn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface SanctnLnRepository extends JpaRepository<SanctnLn, Long>, SanctnLnRepositoryCustom {

    @Query(value = "select sln.SANCTN_DATE ," +
            "sln.SANCTN_OPINION ," +
            "sln.SANCTN_STEP ," +
            "sln.SANCTN_LS_APV ," +
            "sln.SANCTN_STTUS ," +
            "m.MBER_NM ," +
            "sln.MBER_NO ," +
            "mj.MAJOR_NM_KR " +
            " from SANCTN_LN sln, " +
            "MBER m, " +
            "STDNT s, " +
            "MAJOR mj, " +
            "SANCTN sn " +
            " WHERE sn.SANCTN_SN = :id" +
            " AND sln.SANCTN_SN = sn.SANCTN_SN" +
            " AND sln.MBER_NO = m.MBER_NO" +
            " AND m.MBER_NO = s.MBER_NO " +
            " AND s.MAJOR_CODE = mj.MAJOR_CODE" ,nativeQuery = true)
    public Map<String, Object> viewCompliment(@Param("id") Long id);




}
