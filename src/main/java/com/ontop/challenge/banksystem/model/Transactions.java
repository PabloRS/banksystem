package com.ontop.challenge.banksystem.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("transactions")
@Data
@CompoundIndexes({@CompoundIndex(name = "transactions", def = "{'transactionId':1, 'userId':1}", unique = true)})
public class Transactions {

    @Id
    String id;
    String transactionId;
    String userId;
    Integer amount;
    Status status;
    @Indexed(name = "creationDate", direction = IndexDirection.DESCENDING)
    Instant creationDate;
}
