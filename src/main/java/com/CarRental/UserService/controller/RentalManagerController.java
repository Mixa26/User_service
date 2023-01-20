package com.CarRental.UserService.controller;

import com.CarRental.UserService.dto.RentalManagerDto;
import com.CarRental.UserService.dto.CreateRentalManagerDto;
import com.CarRental.UserService.dto.TokenRequestDto;
import com.CarRental.UserService.dto.TokenResponseDto;
import com.CarRental.UserService.security.CheckSecurity;
import com.CarRental.UserService.security.TokenService;
import com.CarRental.UserService.service.RentalManagerService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/rentalManager")
public class RentalManagerController {
    @Autowired
    private RentalManagerService rentalManagerService;

    private TokenService tokenService;

    public RentalManagerController(RentalManagerService rentalManagerService, TokenService tokenService) {
        this.rentalManagerService = rentalManagerService;
        this.tokenService = tokenService;
    }

    @GetMapping("{username}")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<RentalManagerDto> getRentalManager(@RequestHeader("Authorization") String authorization, @PathVariable String username)
    {
        return new ResponseEntity<>(rentalManagerService.findRentalManager(username), HttpStatus.OK);
    }

    @GetMapping("/all")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<Page<RentalManagerDto>> getAllRentalManager(@RequestHeader("Authorization") String authorization, Pageable pageable)
    {
        return new ResponseEntity<>(rentalManagerService.findAll(pageable), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RentalManagerDto> addRentalManager(@RequestBody CreateRentalManagerDto rentalManagerDto)
    {
        return new ResponseEntity<>(rentalManagerService.createRentalManager(rentalManagerDto), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<RentalManagerDto> updateRentalManager(@RequestHeader("Authorization") String authorization, @RequestBody CreateRentalManagerDto rentalManagerDto, @PathVariable Long id)
    {
        //check if client is trying to delete his own profile
        String token = null;
        if (authorization.toString().startsWith("Bearer")) {
            //Get token
            token = authorization.toString().split(" ")[1];
        }
        Claims claims = tokenService.parseToken(token);
        if (claims.get("role", String.class).equals("ROLE_MANAGER"))
        {
            if (!Objects.equals(id, (long)claims.get("id", Integer.class)))
            {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

        return new ResponseEntity<>(rentalManagerService.updateRentalManager(rentalManagerDto), HttpStatus.OK);
    }

    @PutMapping("/canLogin")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<RentalManagerDto> canLoginRentalManager(@RequestHeader("Authorization") String authorization, @RequestBody CreateRentalManagerDto rentalManagerDto)
    {
        return new ResponseEntity<>(rentalManagerService.canLoginRentalManager(rentalManagerDto), HttpStatus.OK);
    }

    @DeleteMapping
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<?> deleteRentalManager(@RequestHeader("Authorization") String authorization, Long id)
    {
        //check if client is trying to delete his own profile
        String token = null;
        if (authorization.toString().startsWith("Bearer")) {
            //Get token
            token = authorization.toString().split(" ")[1];
        }
        Claims claims = tokenService.parseToken(token);
        if (claims.get("role", String.class).equals("ROLE_MANAGER"))
        {
            if (!Objects.equals(id, (long)claims.get("id", Integer.class)))
            {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

        rentalManagerService.deleteRentalManager(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/activation/{salt}")
    public ResponseEntity<?> confirmUser(@PathVariable String salt) {
        rentalManagerService.confirmUser(salt);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(rentalManagerService.login(tokenRequestDto), HttpStatus.OK);
    }
}
