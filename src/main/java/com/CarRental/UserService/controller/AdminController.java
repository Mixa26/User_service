package com.CarRental.UserService.controller;

import com.CarRental.UserService.dto.AdminDto;
import com.CarRental.UserService.dto.CreateAdminDto;
import com.CarRental.UserService.dto.TokenRequestDto;
import com.CarRental.UserService.dto.TokenResponseDto;
import com.CarRental.UserService.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public ResponseEntity<AdminDto> getAdmin(@RequestParam String username)
    {
        return new ResponseEntity<>(adminService.findAdmin(username), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AdminDto> addAdmin(@RequestBody CreateAdminDto adminDto)
    {
        return new ResponseEntity<>(adminService.createAdmin(adminDto), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<AdminDto> updateAdmin(@RequestBody CreateAdminDto adminDto)
    {
        return new ResponseEntity<>(adminService.updateAdmin(adminDto), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAdmin(Long id)
    {
        adminService.deleteAdmin(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(adminService.login(tokenRequestDto), HttpStatus.OK);
    }
}
