package com.CarRental.UserService.controller;

import com.CarRental.UserService.dto.ClientDto;
import com.CarRental.UserService.dto.CreateClientDto;
import com.CarRental.UserService.dto.DiscountDto;
import com.CarRental.UserService.security.CheckSecurity;
import com.CarRental.UserService.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<ClientDto> getClient(@RequestHeader("Authorization") String authorization, @RequestParam String username)
    {
        return new ResponseEntity<>(clientService.findClient(username), HttpStatus.OK);
    }

    @GetMapping("/all")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<Page<ClientDto>> getAllClient(@RequestHeader("Authorization") String authorization, @RequestParam String username, Pageable pageable)
    {
        return new ResponseEntity<>(clientService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}/discount")
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<DiscountDto> getAllClient(@RequestHeader("Authorization") String authorization, @PathVariable("id") Long id)
    {
        return new ResponseEntity<>(clientService.findDiscount(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ClientDto> addClient(@RequestBody CreateClientDto clientDto)
    {
        return new ResponseEntity<>(clientService.createClient(clientDto), HttpStatus.OK);
    }

    @PutMapping
    @CheckSecurity(roles = {"ROLE_ADMIN","ROLE_CLIENT"})
    public ResponseEntity<ClientDto> updateClient(@RequestHeader("Authorization") String authorization, @RequestBody CreateClientDto clientDto)
    {
        return new ResponseEntity<>(clientService.updateClient(clientDto), HttpStatus.OK);
    }

    @PutMapping("/canLogin")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<ClientDto> canLoginClient(@RequestHeader("Authorization") String authorization, @RequestBody CreateClientDto clientDto)
    {
        return new ResponseEntity<>(clientService.canLoginClient(clientDto), HttpStatus.OK);
    }

    @DeleteMapping
    @CheckSecurity(roles = {"ROLE_ADMIN","ROLE_CLIENT"})
    public ResponseEntity<?> deleteClient(@RequestHeader("Authorization") String authorization, Long id)
    {
        clientService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
