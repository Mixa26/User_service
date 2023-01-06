package com.CarRental.UserService.controller;

import com.CarRental.UserService.dto.RentalManagerDto;
import com.CarRental.UserService.dto.CreateRentalManagerDto;
import com.CarRental.UserService.security.CheckSecurity;
import com.CarRental.UserService.service.RentalManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rentalManager")
public class RentalManagerController {
    @Autowired
    private RentalManagerService rentalManagerService;

    public RentalManagerController(RentalManagerService rentalManagerService) {
        this.rentalManagerService = rentalManagerService;
    }

    @GetMapping
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<RentalManagerDto> getRentalManager(@RequestHeader("Authorization") String authorization, @RequestParam String username)
    {
        return new ResponseEntity<>(rentalManagerService.findRentalManager(username), HttpStatus.OK);
    }

    @GetMapping("/all")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<Page<RentalManagerDto>> getAllRentalManager(@RequestHeader("Authorization") String authorization, @RequestParam String username, Pageable pageable)
    {
        return new ResponseEntity<>(rentalManagerService.findAll(pageable), HttpStatus.OK);
    }

    @PostMapping
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<RentalManagerDto> addRentalManager(@RequestHeader("Authorization") String authorization, @RequestBody CreateRentalManagerDto rentalManagerDto)
    {
        return new ResponseEntity<>(rentalManagerService.createRentalManager(rentalManagerDto), HttpStatus.OK);
    }

    @PutMapping
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<RentalManagerDto> updateRentalManager(@RequestHeader("Authorization") String authorization, @RequestBody CreateRentalManagerDto rentalManagerDto)
    {
        return new ResponseEntity<>(rentalManagerService.updateRentalManager(rentalManagerDto), HttpStatus.OK);
    }

    @DeleteMapping
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<?> deleteRentalManager(@RequestHeader("Authorization") String authorization, Long id)
    {
        rentalManagerService.deleteRentalManager(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
