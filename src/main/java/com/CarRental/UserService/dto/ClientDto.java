package com.CarRental.UserService.dto;

public class ClientDto {
    private String username;
    private String name;
    private String surname;
    private String email;
    //additional for client
    private Integer totalRentalTimeInDays;
    private Integer clientRank;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
