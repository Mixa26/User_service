package com.CarRental.UserService.mapper;

import com.CarRental.UserService.domain.RentalManager;
import com.CarRental.UserService.dto.RentalManagerDto;
import com.CarRental.UserService.dto.CreateRentalManagerDto;

public class RentalManagerMapper {
    public RentalManagerDto RentalManagerToRentalManagerDto(RentalManager rentalManager)
    {
        RentalManagerDto rentalManagerDto = new RentalManagerDto();
        rentalManagerDto.setUsername(rentalManager.getUsername());
        rentalManagerDto.setName(rentalManager.getName());
        rentalManagerDto.setSurname(rentalManager.getSurname());
        rentalManagerDto.setEmail(rentalManager.getEmail());
        rentalManagerDto.setCompanyName(rentalManager.getCompanyName());
        rentalManagerDto.setDateOfHire(rentalManager.getDateOfHire());

        return rentalManagerDto;
    }

    public RentalManager RentalManagerDtoToRentalManager(CreateRentalManagerDto rentalManagerDto)
    {
        RentalManager rentalManager = new RentalManager();
        rentalManager.setUsername(rentalManagerDto.getUsername());
        rentalManager.setPassword(rentalManagerDto.getPassword());
        rentalManager.setEmail(rentalManagerDto.getEmail());
        rentalManager.setPhoneNum(rentalManagerDto.getPhoneNum());
        rentalManager.setDateOfBirth(rentalManagerDto.getDateOfBirth());
        rentalManager.setName(rentalManagerDto.getName());
        rentalManager.setSurname(rentalManagerDto.getSurname());
        //additional for RentalManager
        rentalManager.setCompanyName(rentalManagerDto.getCompanyName());
        rentalManager.setDateOfHire(rentalManagerDto.getDateOfHire());

        return rentalManager;
    }
}
