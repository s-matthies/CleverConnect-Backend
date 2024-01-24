package com.example.api.Repository;

import com.example.api.Entitys.SpecialField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecialFieldRepository extends JpaRepository <SpecialField, Long>{

    Optional<SpecialField> findByName(String name);
}
