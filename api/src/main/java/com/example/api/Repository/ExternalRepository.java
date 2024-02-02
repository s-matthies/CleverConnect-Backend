package com.example.api.Repository;

import com.example.api.Entitys.External;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository für die Speicherung von Externen
 */
@Repository
public interface ExternalRepository extends JpaRepository<External, Long> {

    // Userin (Externe) mittels E-Mail in der DB finden
    Optional<External> findByEmail(String email);

}
