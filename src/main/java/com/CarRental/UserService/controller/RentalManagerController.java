package com.CarRental.UserService.controller;

import com.CarRental.UserService.dto.RentalManagerDto;
import com.CarRental.UserService.dto.CreateRentalManagerDto;
import com.CarRental.UserService.service.RentalManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rentalManager")
public class RentalManagerController {
    private RentalManagerService rentalManagerService;

    public RentalManagerController(RentalManagerService rentalManagerService) {
        this.rentalManagerService = rentalManagerService;
    }

    @GetMapping
    public ResponseEntity<RentalManagerDto> getRentalManager(@RequestParam String username)
    {
        return new ResponseEntity<>(rentalManagerService.findRentalManager(username), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RentalManagerDto> addRentalManager(@RequestBody CreateRentalManagerDto rentalManagerDto)
    {
        return new ResponseEntity<>(rentalManagerService.createRentalManager(rentalManagerDto), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<RentalManagerDto> updateRentalManager(@RequestBody CreateRentalManagerDto rentalManagerDto)
    {
        return new ResponseEntity<>(rentalManagerService.updateRentalManager(rentalManagerDto), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteRentalManager(Long id)
    {
        rentalManagerService.deleteRentalManager(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
