package com.ontop.challenge.banksystem.service.impl;

import com.ontop.challenge.banksystem.exceptionhandler.UserNotFoundException;
import com.ontop.challenge.banksystem.model.BalanceResponse;
import com.ontop.challenge.banksystem.model.BankInfo;
import com.ontop.challenge.banksystem.repository.BankInfoRepository;
import com.ontop.challenge.banksystem.service.WalletService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class WalletServiceImpl implements WalletService {

    private final BankInfoRepository bankInfoRepository;

    @Value("${wallet.transactions.fee}")
    private BigDecimal fee;

    RestTemplate restTemplate = new RestTemplate();

    public WalletServiceImpl(BankInfoRepository bankInfoRepository) {
        this.bankInfoRepository = bankInfoRepository;
    }

    @Override
    public BalanceResponse getBalance(String user_id) {
        bankInfoRepository.findByUserId(user_id).orElseThrow(() -> new UserNotFoundException("User not registered"));
        String balanceUrl = "http://mockoon.tools.getontop.com:3000/wallets/balance?user_id="+user_id;
        BalanceResponse balanceResponse = restTemplate.getForObject(balanceUrl, BalanceResponse.class);
        return balanceResponse;
    }

    @Override
    public BankInfo configureBankInfo(BankInfo bankInfo) {
        BankInfo response = bankInfoRepository.findByUserIdAndAccountNumber(bankInfo.getUserId(), bankInfo.getAccountNumber())
                .orElseGet(() -> bankInfoRepository.save(bankInfo));
        return updateBankInfo(response, bankInfo);
    }

    private BankInfo updateBankInfo(BankInfo currentInfo, BankInfo bankInfoUpdate) {
        if(!currentInfo.equals(bankInfoUpdate)) {
            currentInfo.setFirstName(bankInfoUpdate.getFirstName());
            currentInfo.setLastName(bankInfoUpdate.getLastName());
            currentInfo.setRoutingNumber(bankInfoUpdate.getRoutingNumber());
            currentInfo.setNiNumber(bankInfoUpdate.getNiNumber());
            currentInfo.setBankName(bankInfoUpdate.getBankName());
            bankInfoRepository.save(currentInfo);
        }
        return currentInfo;
    }
}
