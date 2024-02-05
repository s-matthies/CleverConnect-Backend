package com.example.api.Service;

import com.example.api.NotFoundExceptions.BachelorSubjectNotFoundException;
import com.example.api.Entitys.BachelorSubject;
import com.example.api.Repository.BachelorSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Klasse für die BachelorSubject Entity
 */
@Service
public class BachelorSubjectService {

    @Autowired
    private final BachelorSubjectRepository bachelorSubjectRepository;

    /**
     * Konstruktor
     * @param bachelorSubjectRepository
     */
    public BachelorSubjectService(BachelorSubjectRepository bachelorSubjectRepository)
    {
        this.bachelorSubjectRepository = bachelorSubjectRepository;
    }

    /**
     * Methode um alle BachelorSubjects zu bekommen
     * @return Liste von BachelorSubjects
     */
    public List<BachelorSubject> allBachelorSubjects() {
        return bachelorSubjectRepository.findAll();
    }


    /**
     * Methode um ein BachelorSubject zu bekommen
     * @param id - ID des BachelorSubjects
     * @return BachelorSubject
     */
    public BachelorSubject getBachelorsubject(Long id) {
        return bachelorSubjectRepository.findById(id)
                .orElseThrow(() -> new BachelorSubjectNotFoundException(id));
    }
}
