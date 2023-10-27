package com.ontop.challenge.banksystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionRequest {

    String amount;
    String user_id;

}
