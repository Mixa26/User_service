package com.CarRental.UserService.domain;

import javax.persistence.*;

@Entity
@Table(indexes = {@Index(columnList = "username", unique = true)})
public class RentalManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true)
    private String username;
    private String password;
    @Column(unique=true)
    private String email;
    @Column(unique=true)
    private String phoneNum;
    private String dateOfBirth;
    private String name;
    private String surname;

    private String registered;

    private boolean canLogin;

    @ManyToOne(optional = false)
    private Role role;
    //additional for rental menager
    @Column(unique=true)
    private String companyName;
    private String dateOfHire;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDateOfHire() {
        return dateOfHire;
    }

    public void setDateOfHire(String dateOfHire) {
        this.dateOfHire = dateOfHire;
    }

    public boolean isCanLogin() {
        return canLogin;
    }

    public void setCanLogin(boolean canLogin) {
        this.canLogin = canLogin;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }
}
