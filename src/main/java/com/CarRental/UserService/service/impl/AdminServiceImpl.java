package com.CarRental.UserService.service.impl;

import com.CarRental.UserService.domain.Admin;
import com.CarRental.UserService.dto.*;
import com.CarRental.UserService.exceptions.NotFoundException;
import com.CarRental.UserService.mapper.AdminMapper;
import com.CarRental.UserService.repository.AdminRepository;
import com.CarRental.UserService.service.AdminService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    private AdminRepository adminRepository;
    private AdminMapper adminMapper;

    public AdminServiceImpl(AdminRepository adminRepository, AdminMapper adminMapper) {
        this.adminRepository = adminRepository;
        this.adminMapper = adminMapper;
    }

    @Override
    public AdminDto findAdmin(String username) {
        return adminMapper.adminToAdminDto(adminRepository.findByUsername(username));
    }

    @Override
    public Page<AdminDto> findAll(Pageable pageable) {
        return adminRepository.findAll(pageable).map(adminMapper::adminToAdminDto);
    }

    @Override
    public AdminDto createAdmin(CreateAdminDto adminDto) {
        Admin admin = adminMapper.adminDtoToAdmin(adminDto);
        adminRepository.save(admin);
        return adminMapper.adminToAdminDto(admin);
    }

    @Override
    public AdminDto updateAdmin(CreateAdminDto adminDto) {
        Admin admin = adminRepository.findByUsername(adminDto.getName());
        admin.setUsername(adminDto.getUsername());
        admin.setPassword(adminDto.getPassword());
        admin.setEmail(adminDto.getEmail());
        admin.setPhoneNum(adminDto.getPhoneNum());
        admin.setDateOfBirth(adminDto.getDateOfBirth());
        admin.setName(adminDto.getName());
        admin.setSurname(admin.getSurname());
        adminRepository.save(admin);
        return adminMapper.adminToAdminDto(admin);
    }

    @Override
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto){
        Admin admin = adminRepository
                .findByUsernameAndPassword(tokenRequestDto.getUsername(), tokenRequestDto.getPassword())
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with username: %s and password: %s not found.", tokenRequestDto.getUsername(),
                                tokenRequestDto.getPassword())));


        Claims claims = Jwts.claims();
        claims.put("id", admin.getId());

        return new TokenResponseDto();
    }
}
