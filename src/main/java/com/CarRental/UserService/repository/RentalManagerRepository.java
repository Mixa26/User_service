package com.CarRental.UserService.repository;

import com.CarRental.UserService.domain.RentalManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RentalManagerRepository extends JpaRepository<RentalManager, Long> {

    RentalManager findByUsername(String username);

    Optional<RentalManager> findByEmailAndPassword(String username, String password);

    RentalManager findByRegistered(String registered);
}
