package com.ontop.challenge.banksystem.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document("bank_info")
@Data
@CompoundIndexes({@CompoundIndex(name = "bankInfo", def = "{'userId':1, 'accountNumber':1}", unique = true)})
public class BankInfo {

    @Id
    String id;
    String userId;
    String firstName;
    String lastName;
    String routingNumber;
    String niNumber;
    String accountNumber;
    String bankName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankInfo bankInfo = (BankInfo) o;
        return Objects.equals(getUserId(), bankInfo.getUserId()) && Objects.equals(getFirstName(), bankInfo.getFirstName()) && Objects.equals(getLastName(), bankInfo.getLastName()) && Objects.equals(getRoutingNumber(), bankInfo.getRoutingNumber()) && Objects.equals(getNiNumber(), bankInfo.getNiNumber()) && Objects.equals(getAccountNumber(), bankInfo.getAccountNumber()) && Objects.equals(getBankName(), bankInfo.getBankName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getFirstName(), getLastName(), getRoutingNumber(), getNiNumber(), getAccountNumber(), getBankName());
    }
}
