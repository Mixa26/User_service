package com.CarRental.UserService.repository;

import com.CarRental.UserService.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    public Admin findByUsername(String username);

    public Admin findByUsernameAndPassword(String username, String password);
}
