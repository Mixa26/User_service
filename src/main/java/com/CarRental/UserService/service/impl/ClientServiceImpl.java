package com.CarRental.UserService.service.impl;

import com.CarRental.UserService.domain.Client;
import com.CarRental.UserService.domain.ClientRank;
import com.CarRental.UserService.dto.*;
import com.CarRental.UserService.dto.notifications.RegistrationNotificationDto;
import com.CarRental.UserService.exceptions.NotFoundException;
import com.CarRental.UserService.helper.MessageHelper;
import com.CarRental.UserService.mapper.ClientMapper;
import com.CarRental.UserService.repository.ClientRankRepository;
import com.CarRental.UserService.repository.ClientRepository;
import com.CarRental.UserService.security.TokenService;
import com.CarRental.UserService.service.ClientService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;
    private ClientMapper clientMapper;

    private ClientRankRepository clientRankRepository;

    private TokenService tokenService;

    @Autowired
    private JmsTemplate jmsTemplate;

    private MessageHelper messageHelper;

    private String destination;

    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper, ClientRankRepository clientRankRepository, TokenService tokenService,
                             JmsTemplate jmsTemplate, @Value("${destination.notify}") String destination, MessageHelper messageHelper){
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.clientRankRepository = clientRankRepository;
        this.tokenService = tokenService;
        this.jmsTemplate = jmsTemplate;
        this.destination = destination;
        this.messageHelper = messageHelper;
    }

    @Override
    public ClientDto findClient(String username)
    {
        return clientMapper.ClientToClientDto(clientRepository.findByUsername(username));
    }

    @Override
    public Page<ClientDto> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable).map(clientMapper::ClientToClientDto);
    }

    @Override
    public ClientDto createClient(CreateClientDto clientDto)
    {
        Client client = clientMapper.ClientDtoToClient(clientDto);
        clientRepository.save(client);
        jmsTemplate.convertAndSend(destination, messageHelper.createTextMessage(new RegistrationNotificationDto(clientDto.getName(), clientDto.getSurname(), clientDto.getEmail())));
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
        client.setCanLogin(clientDto.isCanLogin());
        //additional for client
        client.setPassportNum(clientDto.getPassportNum());
        client.setTotalRentalTimeInDays(clientDto.getTotalRentalTimeInDays());
        client.setClientRank(clientDto.getClientRank());
        clientRepository.save(client);
        return clientMapper.ClientToClientDto(client);
    }

    @Override
    public ClientDto canLoginClient(CreateClientDto clientDto)
    {
        Client client = clientRepository.findByUsername(clientDto.getUsername());
        client.setCanLogin(clientDto.isCanLogin());
        clientRepository.save(client);
        return clientMapper.ClientToClientDto(client);
    }

    @Override
    public void deleteClient(Long id)
    {
        clientRepository.deleteById(id);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) throws NotFoundException {
        Client client = clientRepository
                .findByEmailAndPassword(tokenRequestDto.getEmail(), tokenRequestDto.getPassword())
                .orElseThrow(() -> new NotFoundException(String
                    .format("User with username: %s and password: %s not found.", tokenRequestDto.getEmail(),
                            tokenRequestDto.getPassword())));


        Claims claims = Jwts.claims();
        claims.put("id", client.getId());
        claims.put("role", client.getRole());
        return new TokenResponseDto(tokenService.generate(claims));
    }

    @Override
    public DiscountDto findDiscount(Long id) {
        Client client = clientRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with id: %d not found.", id)));
        List<ClientRank> clientRankList = clientRankRepository.findAll();
        int discount = clientRankList.stream()
                .filter(clientRank -> clientRank.getMaxRentalDays() >= client.getTotalRentalTimeInDays()
                        && clientRank.getMinRentalDays() <= client.getTotalRentalTimeInDays())
                .findAny()
                .get()
                .getDiscount();
        return new DiscountDto(discount);
    }           
    
}
