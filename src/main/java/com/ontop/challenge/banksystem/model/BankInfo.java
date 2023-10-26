package com.ontop.challenge.banksystem.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

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

}
