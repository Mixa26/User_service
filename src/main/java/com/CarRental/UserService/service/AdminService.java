package com.CarRental.UserService.service;

import com.CarRental.UserService.dto.*;
import com.CarRental.UserService.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService{

    AdminDto findAdmin(String username);

    Page<AdminDto> findAll(Pageable pageable);

    AdminDto createAdmin(CreateAdminDto admin);

    AdminDto updateAdmin(CreateAdminDto admin);

    void deleteAdmin(Long id);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);
}
