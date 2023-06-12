package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email,Long> {

    Email findByEmail(String email);
    Email findByClientId(Long id);
}
