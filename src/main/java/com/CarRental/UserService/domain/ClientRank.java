package com.CarRental.UserService.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ClientRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int minRentalDays;

    private int maxRentalDays;

    private String rankName;

    private int discount;

    public ClientRank(){
    }

    public ClientRank(int minRentalDays, int maxRentalDays, String rankName, int discount) {
        this.minRentalDays = minRentalDays;
        this.maxRentalDays = maxRentalDays;
        this.rankName = rankName;
        this.discount = discount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMinRentalDays() {
        return minRentalDays;
    }

    public void setMinRentalDays(int minRentalDays) {
        this.minRentalDays = minRentalDays;
    }

    public int getMaxRentalDays() {
        return maxRentalDays;
    }

    public void setMaxRentalDays(int maxRentalDays) {
        this.maxRentalDays = maxRentalDays;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
