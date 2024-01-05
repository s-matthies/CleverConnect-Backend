package com.example.api.Repository;

import com.example.api.Entitys.BachelorSubject;
import com.example.api.Entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BachelorSubjectRepository  extends JpaRepository <BachelorSubject, Long> {

    Optional<BachelorSubject> findByTitleContaining(String title);

    Optional<BachelorSubject> findByTitle(String title);


}
