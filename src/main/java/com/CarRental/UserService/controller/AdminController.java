package com.CarRental.UserService.controller;

import com.CarRental.UserService.dto.AdminDto;
import com.CarRental.UserService.dto.CreateAdminDto;
import com.CarRental.UserService.dto.TokenRequestDto;
import com.CarRental.UserService.dto.TokenResponseDto;
import com.CarRental.UserService.security.CheckSecurity;
import com.CarRental.UserService.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<AdminDto> getAdmin(@RequestHeader("Authorization") String authorization, @RequestParam String username)
    {
        return new ResponseEntity<>(adminService.findAdmin(username), HttpStatus.OK);
    }

    @GetMapping("/all")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<Page<AdminDto>> getAllAdmin(@RequestHeader("Authorization") String authorization, Pageable pageable)
    {
        return new ResponseEntity<>(adminService.findAll(pageable), HttpStatus.OK);
    }

    //admins are manually added into the database
//    @PostMapping
//    public ResponseEntity<AdminDto> addAdmin(@RequestBody CreateAdminDto adminDto)
//    {
//        return new ResponseEntity<>(adminService.createAdmin(adminDto), HttpStatus.OK);
//    }

    @PutMapping
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<AdminDto> updateAdmin(@RequestHeader("Authorization") String authorization, @RequestBody CreateAdminDto adminDto)
    {
        return new ResponseEntity<>(adminService.updateAdmin(adminDto), HttpStatus.OK);
    }

    @DeleteMapping
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<?> deleteAdmin(@RequestHeader("Authorization") String authorization, Long id)
    {
        adminService.deleteAdmin(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<TokenResponseDto> loginUser(@RequestHeader("Authorization") String authorization, @RequestBody @Valid TokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(adminService.login(tokenRequestDto), HttpStatus.OK);
    }
}
