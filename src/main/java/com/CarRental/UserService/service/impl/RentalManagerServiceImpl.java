package com.CarRental.UserService.service.impl;

import com.CarRental.UserService.domain.Client;
import com.CarRental.UserService.domain.RentalManager;
import com.CarRental.UserService.dto.CreateRentalManagerDto;
import com.CarRental.UserService.dto.RentalManagerDto;
import com.CarRental.UserService.dto.TokenRequestDto;
import com.CarRental.UserService.dto.TokenResponseDto;
import com.CarRental.UserService.exceptions.NotFoundException;
import com.CarRental.UserService.mapper.RentalManagerMapper;
import com.CarRental.UserService.repository.RentalManagerRepository;
import com.CarRental.UserService.service.RentalManagerService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RentalManagerServiceImpl implements RentalManagerService {
    private RentalManagerRepository rentalManagerRepository;
    private RentalManagerMapper rentalManagerMapper;

    public RentalManagerServiceImpl(RentalManagerRepository rentalManagerRepository, RentalManagerMapper rentalManagerMapper) {
        this.rentalManagerRepository = rentalManagerRepository;
        this.rentalManagerMapper = rentalManagerMapper;
    }

    @Override
    public RentalManagerDto findRentalManager(String username) {
        return rentalManagerMapper.RentalManagerToRentalManagerDto(rentalManagerRepository.findByUsername(username));
    }

    @Override
    public Page<RentalManagerDto> findAll(Pageable pageable) {
        return rentalManagerRepository.findAll(pageable).map(rentalManagerMapper::RentalManagerToRentalManagerDto);
    }

    @Override
    public RentalManagerDto createRentalManager(CreateRentalManagerDto rentalManagerDto) {
        RentalManager rentalManager = rentalManagerMapper.RentalManagerDtoToRentalManager(rentalManagerDto);
        rentalManagerRepository.save(rentalManager);
        return rentalManagerMapper.RentalManagerToRentalManagerDto(rentalManager);
    }

    @Override
    public RentalManagerDto updateRentalManager(CreateRentalManagerDto rentalManagerDto) {
        RentalManager rentalManager = rentalManagerRepository.findByUsername(rentalManagerDto.getUsername());
        rentalManager.setUsername(rentalManagerDto.getUsername());
        rentalManager.setPassword(rentalManagerDto.getPassword());
        rentalManager.setEmail(rentalManagerDto.getEmail());
        rentalManager.setPhoneNum(rentalManagerDto.getPhoneNum());
        rentalManager.setDateOfBirth(rentalManagerDto.getDateOfBirth());
        rentalManager.setName(rentalManagerDto.getName());
        rentalManager.setSurname(rentalManagerDto.getSurname());
        //additional for client
        rentalManager.setCompanyName(rentalManagerDto.getCompanyName());
        rentalManager.setDateOfHire(rentalManagerDto.getDateOfHire());
        rentalManagerRepository.save(rentalManager);
        return rentalManagerMapper.RentalManagerToRentalManagerDto(rentalManager);
    }

    @Override
    public void deleteRentalManager(Long id) {
        rentalManagerRepository.deleteById(id);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) throws NotFoundException {
        RentalManager rentalManager = rentalManagerRepository
                .findByEmailAndPassword(tokenRequestDto.getEmail(), tokenRequestDto.getPassword())
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with username: %s and password: %s not found.", tokenRequestDto.getEmail(),
                                tokenRequestDto.getPassword())));


        Claims claims = Jwts.claims();
        claims.put("id", rentalManager.getId());

        return new TokenResponseDto();
    }
}
