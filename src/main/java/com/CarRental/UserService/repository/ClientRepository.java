package com.CarRental.UserService.repository;

import com.CarRental.UserService.domain.Admin;
import com.CarRental.UserService.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    public Client findByUsername(String username);

    public Client findByUsernameAndPassword(String username, String password);
}
