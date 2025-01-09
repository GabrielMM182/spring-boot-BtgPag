package com.projetoBtgPag.projetoBtg.entity;

import jakarta.validation.constraints.*;

import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;

public class OrderItem {

    @NotNull
    private String product;
    @NotNull
    @Positive
    private Integer quantity;
    @NotNull
    @PositiveOrZero
    @Field(targetType = FieldType.DECIMAL128)

    private BigDecimal price;

    public OrderItem() {
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
