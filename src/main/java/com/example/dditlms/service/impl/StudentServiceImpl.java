package com.example.dditlms.service.impl;

import com.example.dditlms.domain.common.AcademicStatus;
import com.example.dditlms.domain.common.Grade;
import com.example.dditlms.domain.common.ScholarshipMethod;
import com.example.dditlms.domain.common.ResultStatus;
import com.example.dditlms.domain.dto.StudentDTO;
import com.example.dditlms.domain.entity.*;
import com.example.dditlms.domain.repository.HistoryRepository;
import com.example.dditlms.domain.repository.RegistrationRepository;
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

    private final RegistrationRepository registrationRepository;

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
    public List<StudentDTO> searchNotRegistedStudents(String category,String search) {
        Optional<SemesterByYear> semesterWrapper = semesterByYearRepository.selectNextSeme();
        SemesterByYear semester = semesterWrapper.orElse(null);
        List<Student> studentList = null;
        if(category.equals("name")){
            studentList = studentRepository.searchNameNotRegistedStudent(semester.getYear(),search);
        }else{
            studentList = studentRepository.searchNumNotRegistedStudent(semester.getYear(),search);
        }
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
        List<Scholarship> scholarshipList = scholarshipRepository.findAllByStudentAndSemesterAndStatusAndMethod(student,semester, ResultStatus.APPROVAL, ScholarshipMethod.REDUCTION);
        long finalAmount = 0;
        for(Scholarship scholarship : scholarshipList){
            finalAmount += scholarship.getPrice();
        }
        finalAmount = student.getMajor().getPayment() - finalAmount;
        int seme = 2;
        Grade grade = student.getGrade();
        if(student.getSemester()==2){
            seme = 1;
            if(grade.equals(Grade.FRESHMAN)){
                grade = Grade.SOPHOMORE;
            }else if(grade.equals(Grade.SOPHOMORE)){
                grade = Grade.JUNIOR;
            }else if(grade.equals(Grade.JUNIOR)){
                grade = Grade.SENIOR;
            }else if(grade.equals(Grade.SENIOR)){
                grade = Grade.EXTRA;
                seme = 0;
            }
        }
        if(grade.equals(Grade.ADMISSION)){
            grade = Grade.FRESHMAN;
            seme = 1;
        }
        Registration registration = Registration.builder()
                .registDate(new Date())
                .aplicationSemester(semester)
                .amount(student.getMajor().getPayment())
                .finalAmount(finalAmount)
                .grade(grade)
                .student(student).build();
        student.setGrade(grade);
        student.setSemester(seme);
        studentRepository.save(student);
        registrationRepository.save(registration);
    }
}
