package com.example.dditlms.service.impl;

import com.example.dditlms.domain.common.AcademicStatus;
import com.example.dditlms.domain.common.ScholarshipMethod;
import com.example.dditlms.domain.common.ResultStatus;
import com.example.dditlms.domain.dto.StudentDTO;
import com.example.dditlms.domain.entity.*;
import com.example.dditlms.domain.repository.HistoryRepository;
import com.example.dditlms.domain.repository.ScholarshipRepository;
import com.example.dditlms.domain.repository.SemesterByYearRepository;
import com.example.dditlms.domain.repository.student.StudentRepository;
import com.example.dditlms.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    private final SemesterByYearRepository semesterByYearRepository;

    private final ScholarshipRepository scholarshipRepository;

    private final HistoryRepository historyRepository;

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> getNotRegistedStudents() {
        Optional<SemesterByYear> semesterWrapper = semesterByYearRepository.selectNextSeme();
        SemesterByYear semester = semesterWrapper.orElse(null);
        List<Student> studentList = studentRepository.getNotRegistedStudent(semester.getYear());
        List<StudentDTO> studentDTOList = new ArrayList<>();
        for(Student student : studentList){
            StudentDTO studentDTO = student.EntitytoDTO();
            List<Scholarship> scholarshipList = scholarshipRepository.findAllByStudentAndSemesterAndStatusAndMethod(student,semester, ResultStatus.APPROVAL, ScholarshipMethod.REDUCTION);
            long realPayment = 0;
            for(Scholarship scholarship : scholarshipList){
                realPayment += scholarship.getPrice();
            }
            realPayment = student.getMajor().getPayment() - realPayment;
            studentDTO.setRealPayment(realPayment);
            studentDTOList.add(studentDTO);
        }
        return studentDTOList;
    }

    @Override
    @Transactional
    public void notPayTuition(Long id) {
        Optional<Student> studentWrapper =studentRepository.findById(id);
        Student student = studentWrapper.orElse(null);
        student.setAcademicStatus(AcademicStatus.EXPULSION);
        History history = History.builder()
                .aplicationDate(new Date())
                .changeDate(new Date())
                .status(AcademicStatus.EXPULSION)
                .student(student).build();
        historyRepository.save(history);
        studentRepository.save(student);
    }

    @Override
    @Transactional
    public void payTuition(Long id) {
        Optional<SemesterByYear> semesterWrapper = semesterByYearRepository.selectNextSeme();
        SemesterByYear semester = semesterWrapper.orElse(null);
        Optional<Student> studentWrapper =studentRepository.findById(id);
        Student student = studentWrapper.orElse(null);
        /*Registration registration = Registration.builder()
                .registDate(new Date())
                .aplicationSemester(semester)
                .amount(student.getMajor().getPayment())
                .f.build();*/

    }
}
