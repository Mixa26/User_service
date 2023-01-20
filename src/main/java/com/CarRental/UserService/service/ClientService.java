package com.CarRental.UserService.service;

import com.CarRental.UserService.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientService {
    ClientDto findClient(String username);

    ClientDto findClient(Long id);

    Page<ClientDto> findAll(Pageable pageable);

    TokenResponseDto createClient(CreateClientDto clientDto);

    ClientDto updateClient(CreateClientDto clientDto);

    ClientDto canLoginClient(String username, boolean canLogin);

    DiscountDto findDiscount(Long id);

    void deleteClient(Long id);

    void confirmUser(String salt);
    TokenResponseDto login(TokenRequestDto tokenRequestDto);

    void updateRentalDays(Long days, Long id);
}
