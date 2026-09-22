package com.guardian.order_service.infrastructure;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductInfo {
    private UUID id;
    private String name;
    private BigDecimal price;

    public ProductInfo() {

    }
    public UUID getId(){
        return id;
    }
    public String getName() {
    return name;
    }
    public BigDecimal getPrice(){
        return price;
    }
    public void setId(UUID id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setPrice(BigDecimal price){
        this.price = price;
    }


}
