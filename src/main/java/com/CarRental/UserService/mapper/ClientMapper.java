package com.CarRental.UserService.mapper;

import com.CarRental.UserService.domain.Client;
import com.CarRental.UserService.dto.ClientDto;
import com.CarRental.UserService.dto.CreateClientDto;
import com.CarRental.UserService.repository.RoleRepository;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    RoleRepository roleRepository;

    public ClientDto ClientToClientDto(Client client)
    {
        ClientDto clientDto = new ClientDto();
        clientDto.setUsername(client.getUsername());
        clientDto.setName(client.getName());
        clientDto.setSurname(client.getSurname());
        clientDto.setEmail(client.getEmail());
        clientDto.setTotalRentalTimeInDays(client.getTotalRentalTimeInDays());
        clientDto.setClientRank(client.getClientRank());

        return clientDto;
    }

    public Client ClientDtoToClient(CreateClientDto clientDto)
    {
        Client client = new Client();
        client.setUsername(clientDto.getUsername());
        client.setPassword(clientDto.getPassword());
        client.setEmail(clientDto.getEmail());
        client.setPhoneNum(clientDto.getPhoneNum());
        client.setDateOfBirth(clientDto.getDateOfBirth());
        client.setName(clientDto.getName());
        client.setSurname(clientDto.getSurname());
        client.setRole(roleRepository.findByName("ROLE_CLIENT"));
        //additional for client
        client.setPassportNum(clientDto.getPassportNum());
        client.setTotalRentalTimeInDays(clientDto.getTotalRentalTimeInDays());
        client.setClientRank(clientDto.getClientRank());

        return client;
    }
}
