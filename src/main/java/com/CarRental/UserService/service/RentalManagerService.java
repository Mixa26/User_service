package com.CarRental.UserService.service;

import com.CarRental.UserService.dto.CreateRentalManagerDto;
import com.CarRental.UserService.dto.RentalManagerDto;
import com.CarRental.UserService.dto.TokenRequestDto;
import com.CarRental.UserService.dto.TokenResponseDto;

public interface RentalManagerService {
    RentalManagerDto findRentalManager(String username);

    RentalManagerDto createRentalManager(CreateRentalManagerDto rentalManagerDto);

    RentalManagerDto updateRentalManager(CreateRentalManagerDto rentalManagerDto);

    void deleteRentalManager(Long id);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);
}
