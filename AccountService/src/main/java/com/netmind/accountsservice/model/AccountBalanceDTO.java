package com.netmind.accountsservice.model;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;

public class AccountBalanceDTO {
    @Id
    @Min(1)
    public Long id;
    @Id
    @Min(1)
    public int amount;
    @Id
    @Min(1)
    public Long ownerId;
}
