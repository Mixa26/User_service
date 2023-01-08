package com.CarRental.UserService.repository;

import com.CarRental.UserService.domain.ClientRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRankRepository extends JpaRepository<ClientRank, Long> {
}
