package com.example.dditlms.service.impl;

import com.example.dditlms.domain.dto.SubjectDTO;
import com.example.dditlms.domain.entity.Subject;
import com.example.dditlms.domain.repository.SubjectRepository;
import com.example.dditlms.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;


    @Override
    public void insertSubject(SubjectDTO subjectDTO) {
        Subject parent = null;
        if(subjectDTO.getParent()!=null && !subjectDTO.getParent().equals("")){
            Optional<Subject> parentWrapper = subjectRepository.findById(subjectDTO.getId());
            parent = parentWrapper.orElse(null);
        }
        Subject subject = Subject.builder()
                .id(subjectDTO.getId())
                .name(subjectDTO.getName())
                .completionDiv(subjectDTO.getCompletionDiv())
                .courseOutline(subjectDTO.getCourseOutline())
                .point(subjectDTO.getPoint())
                .parent(parent)
                .status(subjectDTO.getStatus()).build();
        subjectRepository.save(subject);
    }

    @Override
    public void updateSubject(SubjectDTO subjectDTO) {
        Optional<Subject> subjectWrapper = subjectRepository.findById(subjectDTO.getId());
        Subject subject = subjectWrapper.orElse(null);
        Subject parent = null;
        if(subjectDTO.getParent()!=null && !subjectDTO.getParent().equals("")){
            Optional<Subject> parentWrapper = subjectRepository.findById(subjectDTO.getId());
            parent = parentWrapper.orElse(null);
        }
        subject.setName(subjectDTO.getName());
        subject.setCompletionDiv(subjectDTO.getCompletionDiv());
        subject.setParent(parent);
        subject.setPoint(subjectDTO.getPoint());
        subject.setCourseOutline(subjectDTO.getCourseOutline());
        subject.setStatus(subjectDTO.getStatus());
        subjectRepository.save(subject);
    }
}
