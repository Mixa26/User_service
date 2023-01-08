package com.CarRental.UserService.repository;

import com.CarRental.UserService.domain.RentalManager;
import com.CarRental.UserService.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository  extends JpaRepository<Role, Long> {

    public Role findByName(String name);
}
