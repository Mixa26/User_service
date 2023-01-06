package com.CarRental.UserService.repository;

import com.CarRental.UserService.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findByUsername(String username);

    Optional<Admin> findByUsernameAndPassword(String username, String password);
}
