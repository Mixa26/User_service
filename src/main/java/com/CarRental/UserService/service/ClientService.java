package com.CarRental.UserService.service;

import com.CarRental.UserService.dto.ClientDto;
import com.CarRental.UserService.dto.CreateClientDto;
import com.CarRental.UserService.dto.TokenRequestDto;
import com.CarRental.UserService.dto.TokenResponseDto;

public interface ClientService {
    ClientDto findClient(String username);

    ClientDto createClient(CreateClientDto clientDto);

    ClientDto updateClient(CreateClientDto clientDto);

    void deleteClient(Long id);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);
}
