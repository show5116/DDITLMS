package com.example.dditlms.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface AcademicService {
    void applicationStatus(Map<String, Object> map);

    void historyInsert(Map<String, Object> map);

    void semesterInsert(Map<String, Object> map);

    void cancelService(Map<String, Object> map);

    void tempAbsenceUpdate(Map<String, Object> map);

    void majorChangeHist(Map<String, Object> map);
}
