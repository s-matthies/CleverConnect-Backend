package com.example.api.Service;

import com.example.api.BachelorSubjectNotFound.BachelorSubjectNotFoundException;
import com.example.api.Entitys.BachelorSubject;
import com.example.api.Entitys.User;
import com.example.api.Repository.BachelorSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BachelorSubjectService {

    @Autowired
    private final BachelorSubjectRepository bachelorSubjectRepository;

    public BachelorSubjectService(BachelorSubjectRepository bachelorSubjectRepository)
    {
        this.bachelorSubjectRepository = bachelorSubjectRepository;
    }


    public List<BachelorSubject> allBachelorSubjects() {
        return bachelorSubjectRepository.findAll();
    }

    public BachelorSubject getBachelorsubject(Long id) {
        return bachelorSubjectRepository.findById(id)
                .orElseThrow(() -> new BachelorSubjectNotFoundException(id));
    }
}
