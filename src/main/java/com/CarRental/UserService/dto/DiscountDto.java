package com.CarRental.UserService.dto;

public class DiscountDto {
    private int discount;

    public DiscountDto(int discount) {
        this.discount = discount;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
