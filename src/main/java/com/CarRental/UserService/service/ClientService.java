package com.CarRental.UserService.service;

import com.CarRental.UserService.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientService {
    ClientDto findClient(String username);

    Page<ClientDto> findAll(Pageable pageable);

    ClientDto createClient(CreateClientDto clientDto);

    ClientDto updateClient(CreateClientDto clientDto);

    ClientDto canLoginClient(CreateClientDto clientDto);

    DiscountDto findDiscount(Long id);

    void deleteClient(Long id);

    TokenResponseDto login(TokenRequestDto tokenRequestDto);
}
