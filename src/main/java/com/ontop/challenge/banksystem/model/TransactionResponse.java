package com.ontop.challenge.banksystem.model;

import lombok.Data;

@Data
public class TransactionResponse {

    String wallet_transaction_id;
    String amount;
    String user_id;

}
