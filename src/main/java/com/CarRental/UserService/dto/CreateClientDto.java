package com.CarRental.UserService.dto;

public class CreateClientDto {
    private String username;
    private String password;
    private String email;
    private String phoneNum;
    private String dateOfBirth;
    private String name;
    private String surname;

    private boolean canLogin;

    //additional for client
    private Integer passportNum;
    private Integer totalRentalTimeInDays;
    private Integer clientRank;

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

    public Integer getPassportNum() {
        return passportNum;
    }

    public void setPassportNum(Integer passportNum) {
        this.passportNum = passportNum;
    }

    public Integer getTotalRentalTimeInDays() {
        return totalRentalTimeInDays;
    }

    public void setTotalRentalTimeInDays(Integer totalRentalTimeInDays) {
        this.totalRentalTimeInDays = totalRentalTimeInDays;
    }

    public Integer getClientRank() {
        return clientRank;
    }

    public void setClientRank(Integer clientRank) {
        this.clientRank = clientRank;
    }

    public boolean isCanLogin() {
        return canLogin;
    }

    public void setCanLogin(boolean canLogin) {
        this.canLogin = canLogin;
    }
}
