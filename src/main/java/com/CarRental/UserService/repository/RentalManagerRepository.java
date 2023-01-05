package com.CarRental.UserService.repository;

import com.CarRental.UserService.domain.Admin;
import com.CarRental.UserService.domain.RentalManager;
import com.CarRental.UserService.dto.RentalManagerDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalManagerRepository extends JpaRepository<RentalManager, Long> {

    public RentalManager findByUsername(String username);

    public RentalManager findByUsernameAndPassword(String username, String password);
}
