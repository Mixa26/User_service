package com.CarRental.UserService.service.impl;

import com.CarRental.UserService.domain.Client;
import com.CarRental.UserService.domain.RentalManager;
import com.CarRental.UserService.dto.CreateRentalManagerDto;
import com.CarRental.UserService.dto.RentalManagerDto;
import com.CarRental.UserService.dto.TokenRequestDto;
import com.CarRental.UserService.dto.TokenResponseDto;
import com.CarRental.UserService.dto.notifications.PasswordChangeNotificationDto;
import com.CarRental.UserService.dto.notifications.RegistrationNotificationDto;
import com.CarRental.UserService.exceptions.Forbidden;
import com.CarRental.UserService.exceptions.NotFoundException;
import com.CarRental.UserService.helper.MessageHelper;
import com.CarRental.UserService.mapper.RentalManagerMapper;
import com.CarRental.UserService.repository.RentalManagerRepository;
import com.CarRental.UserService.security.TokenService;
import com.CarRental.UserService.service.RentalManagerService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RentalManagerServiceImpl implements RentalManagerService {
    private RentalManagerRepository rentalManagerRepository;
    private RentalManagerMapper rentalManagerMapper;

    @Autowired
    private JmsTemplate jmsTemplate;

    private MessageHelper messageHelper;
    private TokenService tokenService;

    private String registrationDestination;

    private String passwordChangeDestination;

    private String activationLinkPrefix = "http://localhost:8080/api/rentalManager/activation/";

    public RentalManagerServiceImpl(RentalManagerRepository rentalManagerRepository, RentalManagerMapper rentalManagerMapper, TokenService tokenService,
                                    MessageHelper messageHelper, JmsTemplate jmsTemplate, @Value("${destination.notify}") String registrationDestination,
                                    @Value("${destination.notify}") String passwordChangeDestination) {
        this.rentalManagerRepository = rentalManagerRepository;
        this.rentalManagerMapper = rentalManagerMapper;
        this.tokenService = tokenService;
        this.messageHelper = messageHelper;
        this.jmsTemplate = jmsTemplate;
        this.registrationDestination = registrationDestination;
        this.passwordChangeDestination = passwordChangeDestination;
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
        Random random = new Random();
        StringBuilder saltBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++)
        {
            saltBuilder.append("abcdefghijklmnopqrstuvwxyz".toCharArray()[random.nextInt("abcdefghijklmnopqrstuvwxyz".toCharArray().length)]);
        }
        String salt = saltBuilder.toString();
        rentalManagerDto.setRegistered(salt);

        RentalManager rentalManager = rentalManagerMapper.RentalManagerDtoToRentalManager(rentalManagerDto);
        rentalManagerRepository.save(rentalManager);

        Long id = rentalManagerRepository.findByUsername(rentalManager.getUsername()).getId();

        jmsTemplate.convertAndSend(registrationDestination, messageHelper.createTextMessage(new RegistrationNotificationDto(id, rentalManagerDto.getName(), rentalManagerDto.getSurname(), rentalManagerDto.getEmail(), activationLinkPrefix + rentalManagerDto.getRegistered())));
        return rentalManagerMapper.RentalManagerToRentalManagerDto(rentalManager);
    }

    @Override
    public RentalManagerDto updateRentalManager(CreateRentalManagerDto rentalManagerDto) {
        RentalManager rentalManager = rentalManagerRepository.findByUsername(rentalManagerDto.getUsername());

        boolean passwordChanged = !rentalManagerDto.getPassword().equals(rentalManager.getPassword());

        rentalManager.setUsername(rentalManagerDto.getUsername());
        rentalManager.setPassword(rentalManagerDto.getPassword());
        rentalManager.setEmail(rentalManagerDto.getEmail());
        rentalManager.setPhoneNum(rentalManagerDto.getPhoneNum());
        rentalManager.setDateOfBirth(rentalManagerDto.getDateOfBirth());
        rentalManager.setName(rentalManagerDto.getName());
        rentalManager.setSurname(rentalManagerDto.getSurname());
        rentalManager.setCanLogin(rentalManagerDto.isCanLogin());
        //additional for client
        rentalManager.setCompanyName(rentalManagerDto.getCompanyName());
        rentalManager.setDateOfHire(rentalManagerDto.getDateOfHire());
        rentalManagerRepository.save(rentalManager);
        if (passwordChanged)
        {
            jmsTemplate.convertAndSend(passwordChangeDestination, messageHelper.createTextMessage(new PasswordChangeNotificationDto(rentalManager.getId(), rentalManagerDto.getName(), rentalManagerDto.getSurname(), rentalManagerDto.getUsername(), rentalManagerDto.getEmail())));
        }
        return rentalManagerMapper.RentalManagerToRentalManagerDto(rentalManager);
    }

    @Override
    public RentalManagerDto canLoginRentalManager(CreateRentalManagerDto rentalManagerDto) {
        RentalManager rentalManager = rentalManagerRepository.findByUsername(rentalManagerDto.getUsername());
        rentalManager.setCanLogin(rentalManagerDto.isCanLogin());
        rentalManagerRepository.save(rentalManager);
        return rentalManagerMapper.RentalManagerToRentalManagerDto(rentalManager);
    }

    @Override
    public void deleteRentalManager(Long id) {
        rentalManagerRepository.deleteById(id);
    }

    @Override
    public void confirmUser(String salt) {
        RentalManager rentalManager = rentalManagerRepository.findByRegistered(salt);

        rentalManager.setRegistered("registered");
    }

    @Override
    public TokenResponseDto login(TokenRequestDto tokenRequestDto) throws NotFoundException {
        RentalManager rentalManager = rentalManagerRepository
                .findByEmailAndPassword(tokenRequestDto.getEmail(), tokenRequestDto.getPassword())
                .orElseThrow(() -> new NotFoundException(String
                        .format("User with username: %s and password: %s not found.", tokenRequestDto.getEmail(),
                                tokenRequestDto.getPassword())));

        if (!rentalManager.getRegistered().equals("registered"))
        {
            throw new Forbidden("You haven't confirmed your account through email.");
        }
        if (!rentalManager.isCanLogin())
        {
            throw new Forbidden("You have been forbidden to login by the admin.");
        }

        Claims claims = Jwts.claims();
        claims.put("id", rentalManager.getId());
        claims.put("role", rentalManager.getRole().getName());
        return new TokenResponseDto(tokenService.generate(claims));
    }
}
