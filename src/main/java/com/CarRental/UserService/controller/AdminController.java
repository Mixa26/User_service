package com.CarRental.UserService.controller;

import com.CarRental.UserService.dto.AdminDto;
import com.CarRental.UserService.dto.CreateAdminDto;
import com.CarRental.UserService.dto.TokenRequestDto;
import com.CarRental.UserService.dto.TokenResponseDto;
import com.CarRental.UserService.security.CheckSecurity;
import com.CarRental.UserService.security.TokenService;
import com.CarRental.UserService.service.AdminService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    private TokenService tokenService;

    public AdminController(AdminService adminService, TokenService tokenService) {
        this.adminService = adminService;
        this.tokenService = tokenService;
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

    @PutMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<AdminDto> updateAdmin(@RequestHeader("Authorization") String authorization, @RequestBody CreateAdminDto adminDto, @PathVariable Long id)
    {
        //check if client is trying to delete his own profile
        String token = null;
        if (authorization.toString().startsWith("Bearer")) {
            //Get token
            token = authorization.toString().split(" ")[1];
        }
        Claims claims = tokenService.parseToken(token);
        if (claims.get("role", String.class).equals("ROLE_ADMIN"))
        {
            if (!Objects.equals(id, (long)claims.get("id", Integer.class)))
            {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

        return new ResponseEntity<>(adminService.updateAdmin(adminDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<?> deleteAdmin(@RequestHeader("Authorization") String authorization,@PathVariable Long id)
    {
        //check if client is trying to delete his own profile
        String token = null;
        if (authorization.toString().startsWith("Bearer")) {
            //Get token
            token = authorization.toString().split(" ")[1];
        }
        Claims claims = tokenService.parseToken(token);
        if (claims.get("role", String.class).equals("ROLE_ADMIN"))
        {
            if (!Objects.equals(id, (long)claims.get("id", Integer.class)))
            {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

        adminService.deleteAdmin(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(adminService.login(tokenRequestDto), HttpStatus.OK);
    }
}
