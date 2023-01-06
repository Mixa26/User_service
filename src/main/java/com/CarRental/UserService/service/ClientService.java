package com.CarRental.UserService.service;

import com.CarRental.UserService.dto.*;
import com.CarRental.UserService.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientService {
    ClientDto findClient(String username);

    Page<ClientDto> findAll(Pageable pageable);

    ClientDto createClient(CreateClientDto clientDto);

    ClientDto updateClient(CreateClientDto clientDto);

    void deleteClient(Long id);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);
}
