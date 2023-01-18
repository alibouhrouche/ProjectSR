package com.app.repository;

import com.app.entities.Client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByEmail(String email);

    Page<Client> findByEmail(String e, Pageable p);
}
