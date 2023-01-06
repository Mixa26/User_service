package com.CarRental.UserService.repository;

import com.CarRental.UserService.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByUsername(String username);

    Optional<Client> findByUsernameAndPassword(String username, String password);
}
