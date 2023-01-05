package com.CarRental.UserService.controller;

import com.CarRental.UserService.dto.ClientDto;
import com.CarRental.UserService.dto.CreateClientDto;
import com.CarRental.UserService.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public class ClientController {
    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<ClientDto> getClient(@RequestParam String username)
    {
        return new ResponseEntity<>(clientService.findClient(username), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ClientDto> addClient(@RequestBody CreateClientDto clientDto)
    {
        return new ResponseEntity<>(clientService.createClient(clientDto), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ClientDto> updateClient(@RequestBody CreateClientDto clientDto)
    {
        return new ResponseEntity<>(clientService.updateClient(clientDto), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteClient(Long id)
    {
        clientService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
