package com.CarRental.UserService.mapper;

import com.CarRental.UserService.domain.Admin;
import com.CarRental.UserService.dto.AdminDto;
import com.CarRental.UserService.dto.CreateAdminDto;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {

    public AdminDto adminToAdminDto(Admin admin)
    {
        AdminDto adminDto = new AdminDto();
        adminDto.setUsername(admin.getUsername());
        adminDto.setName(admin.getName());
        adminDto.setSurname(admin.getSurname());
        adminDto.setEmail(admin.getEmail());

        return adminDto;
    }

    public Admin adminDtoToAdmin(CreateAdminDto adminDto)
    {
        Admin admin = new Admin();
        admin.setUsername(adminDto.getUsername());
        admin.setPassword(adminDto.getPassword());
        admin.setEmail(adminDto.getEmail());
        admin.setPhoneNum(adminDto.getPhoneNum());
        admin.setDateOfBirth(adminDto.getDateOfBirth());
        admin.setName(adminDto.getName());
        admin.setSurname(adminDto.getSurname());

        return admin;
    }
}
