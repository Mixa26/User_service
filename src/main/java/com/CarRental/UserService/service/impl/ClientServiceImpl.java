package com.CarRental.UserService.service.impl;

import com.CarRental.UserService.domain.Admin;
import com.CarRental.UserService.domain.Client;
import com.CarRental.UserService.domain.ClientRank;
import com.CarRental.UserService.domain.RentalManager;
import com.CarRental.UserService.dto.*;
import com.CarRental.UserService.dto.notifications.PasswordChangeNotificationDto;
import com.CarRental.UserService.dto.notifications.RegistrationNotificationDto;
import com.CarRental.UserService.exceptions.Forbidden;
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
import java.util.Objects;
import java.util.Random;

@Service
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;
    private ClientMapper clientMapper;

    private ClientRankRepository clientRankRepository;

    private TokenService tokenService;

    @Autowired
    private JmsTemplate jmsTemplate;

    private MessageHelper messageHelper;

    private String registrationDestination;

    private String passwordChangeDestination;

    private String activationLinkPrefix = "http://localhost:8080/api/client/activation/";

    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper, ClientRankRepository clientRankRepository, TokenService tokenService,
                             JmsTemplate jmsTemplate, @Value("${destination.notify}") String registrationDestination, @Value("${destination.passwordChangeNotify}") String passwordChangeDestination, MessageHelper messageHelper){
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.clientRankRepository = clientRankRepository;
        this.tokenService = tokenService;
        this.jmsTemplate = jmsTemplate;
        this.registrationDestination = registrationDestination;
        this.passwordChangeDestination = passwordChangeDestination;
        this.messageHelper = messageHelper;
    }

    @Override
    public ClientDto findClient(String username)
    {
        return clientMapper.ClientToClientDto(clientRepository.findByUsername(username));
    }

    @Override
    public ClientDto findClient(Long id)
    {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with %d id not found!", id)));

        return clientMapper.ClientToClientDto(client);
    }

    @Override
    public Page<ClientDto> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable).map(clientMapper::ClientToClientDto);
    }

    @Override
    public TokenResponseDto createClient(CreateClientDto clientDto)
    {
        Random random = new Random();
        StringBuilder saltBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++)
        {
            saltBuilder.append("abcdefghijklmnopqrstuvwxyz".toCharArray()[random.nextInt("abcdefghijklmnopqrstuvwxyz".toCharArray().length)]);
        }
        String salt = saltBuilder.toString();
        clientDto.setRegistered(salt);
        clientDto.setCanLogin(true);
        clientDto.setTotalRentalTimeInDays((long)0);

        Client client = clientMapper.ClientDtoToClient(clientDto);
        clientRepository.save(client);

        Long id = clientRepository.findByUsername(client.getUsername()).getId();

        Claims claims = Jwts.claims();
        claims.put("id", client.getId());
        claims.put("role", client.getRole().getName());

        jmsTemplate.convertAndSend(registrationDestination, messageHelper.createTextMessage(new RegistrationNotificationDto(id, clientDto.getName(), clientDto.getSurname(), clientDto.getEmail(), activationLinkPrefix + clientDto.getRegistered())));
        return new TokenResponseDto(tokenService.generate(claims));
        //return clientMapper.ClientToClientDto(client);
    }

    @Override
    public ClientDto updateClient(CreateClientDto clientDto)
    {
        Client client = clientRepository.findByUsername(clientDto.getUsername());

        boolean passwordChanged = !clientDto.getPassword().equals(client.getPassword());

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
        clientRepository.save(client);
        if (passwordChanged)
        {
            jmsTemplate.convertAndSend(passwordChangeDestination, messageHelper.createTextMessage(new PasswordChangeNotificationDto(client.getId(), clientDto.getName(), clientDto.getSurname(), clientDto.getUsername(), clientDto.getEmail())));
        }
        return clientMapper.ClientToClientDto(client);
    }

    @Override
    public ClientDto canLoginClient(String username, boolean canLogin)
    {
        Client client = clientRepository.findByUsername(username);
        client.setCanLogin(canLogin);
        clientRepository.save(client);
        return clientMapper.ClientToClientDto(client);
    }

    @Override
    public void confirmUser(String salt) {
        Client client = clientRepository.findByRegistered(salt);
        client.setRegistered("registered");
        clientRepository.save(client);
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto){
        Client client = clientRepository
                .findByEmailAndPassword(tokenRequestDto.getEmail(), tokenRequestDto.getPassword())
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with email: %s and password: %s not found.", tokenRequestDto.getEmail(),
                                tokenRequestDto.getPassword())));

        System.out.println(client.getRegistered());
        if (!client.getRegistered().equals("registered"))
        {
            throw new Forbidden("You haven't confirmed your account through email.");
        }
        if (!client.isCanLogin())
        {
            throw new Forbidden("You have been forbidden to login by the admin.");
        }

        Claims claims = Jwts.claims();
        claims.put("id", client.getId());
        claims.put("role", client.getRole().getName());
        return new TokenResponseDto(tokenService.generate(claims));
    }

    @Override
    public void deleteClient(Long id)
    {
        clientRepository.deleteById(id);
    }

    @Override
    public void updateRentalDays(Long days, Long id) {
        Client client = clientRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with id: %d not found.", id)));

        if (client.getTotalRentalTimeInDays() + days >= 0) {
            client.setTotalRentalTimeInDays(client.getTotalRentalTimeInDays() + days);
        }else{
            client.setTotalRentalTimeInDays((long)0);
        }
        clientRepository.save(client);
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
