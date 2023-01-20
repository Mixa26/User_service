package com.CarRental.UserService.controller;

import com.CarRental.UserService.dto.*;
import com.CarRental.UserService.security.CheckSecurity;
import com.CarRental.UserService.security.TokenService;
import com.CarRental.UserService.service.ClientService;
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
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientService clientService;

    private TokenService tokenService;

    public ClientController(ClientService clientService, TokenService tokenService) {
        this.clientService = clientService;
        this.tokenService = tokenService;
    }

    @GetMapping("/{username}")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<ClientDto> getClient(@RequestHeader("Authorization") String authorization, @PathVariable String username)
    {
        return new ResponseEntity<>(clientService.findClient(username), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_ADMIN, ROLE_CLIENT"})
    public ResponseEntity<ClientDto> getClientById(@RequestHeader("Authorization") String authorization, @PathVariable Long id)
    {
        //check if client is trying to delete his own profile
        String token = null;
        if (authorization.toString().startsWith("Bearer")) {
            //Get token
            token = authorization.toString().split(" ")[1];
        }
        Claims claims = tokenService.parseToken(token);
        if (claims.get("role", String.class).equals("ROLE_CLIENT"))
        {
            if (!Objects.equals(id, (long)claims.get("id", Integer.class)))
            {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

        return new ResponseEntity<>(clientService.findClient(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<Page<ClientDto>> getAllClient(@RequestHeader("Authorization") String authorization, Pageable pageable)
    {
        return new ResponseEntity<>(clientService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}/discount")
    @CheckSecurity(roles = {"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<DiscountDto> getDiscount(@RequestHeader("Authorization") String authorization, @PathVariable("id") Long id)
    {
        return new ResponseEntity<>(clientService.findDiscount(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TokenResponseDto> addClient(@RequestBody CreateClientDto clientDto)
    {
        return new ResponseEntity<>(clientService.createClient(clientDto), HttpStatus.OK);
    }

    @PutMapping("/updateRentalDays/{id}/{days}")
    @CheckSecurity(roles = {"ROLE_ADMIN","ROLE_CLIENT"})
    public ResponseEntity<?> updateRentalDays(@RequestHeader("Authorization") String authorization, @PathVariable Long days, @PathVariable Long id)
    {
        //check if client is trying to delete his own profile
        String token = null;
        if (authorization.toString().startsWith("Bearer")) {
            //Get token
            token = authorization.toString().split(" ")[1];
        }
        Claims claims = tokenService.parseToken(token);
        if (claims.get("role", String.class).equals("ROLE_CLIENT"))
        {
            if (!Objects.equals(id, (long)claims.get("id", Integer.class)))
            {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

        clientService.updateRentalDays(days, id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_ADMIN","ROLE_CLIENT"})
    public ResponseEntity<ClientDto> updateClient(@RequestHeader("Authorization") String authorization, @RequestBody CreateClientDto clientDto, @PathVariable Long id)
    {
        //check if client is trying to delete his own profile
        String token = null;
        if (authorization.toString().startsWith("Bearer")) {
            //Get token
            token = authorization.toString().split(" ")[1];
        }
        Claims claims = tokenService.parseToken(token);
        if (claims.get("role", String.class).equals("ROLE_CLIENT"))
        {
            if (!Objects.equals(id, (long)claims.get("id", Integer.class)))
            {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

        return new ResponseEntity<>(clientService.updateClient(clientDto), HttpStatus.OK);
    }

    @PutMapping("/canLogin/{username}/{canLogin}")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<ClientDto> canLoginClient(@RequestHeader("Authorization") String authorization, @PathVariable String username, @PathVariable boolean canLogin)
    {
        return new ResponseEntity<>(clientService.canLoginClient(username, canLogin), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @CheckSecurity(roles = {"ROLE_ADMIN","ROLE_CLIENT"})
    public ResponseEntity<?> deleteClient(@RequestHeader("Authorization") String authorization,@PathVariable Long id)
    {
        //check if client is trying to delete his own profile
        String token = null;
        if (authorization.toString().startsWith("Bearer")) {
            //Get token
            token = authorization.toString().split(" ")[1];
        }
        Claims claims = tokenService.parseToken(token);
        if (claims.get("role", String.class).equals("ROLE_CLIENT"))
        {
            if (!Objects.equals(id, (long)claims.get("id", Integer.class)))
            {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

        clientService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/activation/{salt}")
    public ResponseEntity<?> confirmUser(@PathVariable String salt) {
        clientService.confirmUser(salt);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(clientService.login(tokenRequestDto), HttpStatus.OK);
    }
}
