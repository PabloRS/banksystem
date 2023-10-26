package com.ontop.challenge.banksystem.service;

import com.ontop.challenge.banksystem.model.BalanceResponse;
import com.ontop.challenge.banksystem.model.BankInfo;

public interface WalletService {

    BalanceResponse getBalance(String user_id);

    BankInfo configureBankInfo(BankInfo bankInfo);

}
