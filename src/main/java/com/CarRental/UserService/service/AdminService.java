package com.CarRental.UserService.service;

import com.CarRental.UserService.dto.AdminDto;
import com.CarRental.UserService.dto.CreateAdminDto;
import com.CarRental.UserService.dto.TokenRequestDto;
import com.CarRental.UserService.dto.TokenResponseDto;

public interface AdminService {

    AdminDto findAdmin(String username);
    AdminDto createAdmin(CreateAdminDto admin);

    AdminDto updateAdmin(CreateAdminDto admin);

    void deleteAdmin(Long id);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);
}
