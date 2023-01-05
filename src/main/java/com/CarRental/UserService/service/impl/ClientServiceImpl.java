package com.CarRental.UserService.service.impl;

import com.CarRental.UserService.domain.Client;
import com.CarRental.UserService.dto.ClientDto;
import com.CarRental.UserService.dto.CreateClientDto;
import com.CarRental.UserService.mapper.ClientMapper;
import com.CarRental.UserService.repository.ClientRepository;
import com.CarRental.UserService.service.ClientService;

public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;
    private ClientMapper clientMapper;

    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    public ClientDto findClient(String username)
    {
        return clientMapper.ClientToClientDto(clientRepository.findByUsername(username));
    }

    @Override
    public ClientDto createClient(CreateClientDto clientDto)
    {
        Client client = clientMapper.ClientDtoToClient(clientDto);
        clientRepository.save(client);
        return clientMapper.ClientToClientDto(client);
    }

    @Override
    public ClientDto updateClient(CreateClientDto clientDto)
    {
        Client client = clientRepository.findByUsername(clientDto.getUsername());
        client.setUsername(clientDto.getUsername());
        client.setPassword(clientDto.getPassword());
        client.setEmail(clientDto.getEmail());
        client.setPhoneNum(clientDto.getPhoneNum());
        client.setDateOfBirth(clientDto.getDateOfBirth());
        client.setName(clientDto.getName());
        client.setSurname(clientDto.getSurname());
        //additional for client
        client.setPassportNum(clientDto.getPassportNum());
        client.setTotalRentalTimeInDays(clientDto.getTotalRentalTimeInDays());
        client.setClientRank(clientDto.getClientRank());
        clientRepository.save(client);
        return clientMapper.ClientToClientDto(client);
    }

    @Override
    public void deleteClient(Long id)
    {
        clientRepository.deleteById(id);
    }
}
