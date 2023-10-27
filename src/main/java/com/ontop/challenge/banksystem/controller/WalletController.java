package com.ontop.challenge.banksystem.controller;

import com.ontop.challenge.banksystem.exceptionhandler.ValidationException;
import com.ontop.challenge.banksystem.model.BalanceResponse;
import com.ontop.challenge.banksystem.model.BankInfo;
import com.ontop.challenge.banksystem.model.Transactions;
import com.ontop.challenge.banksystem.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/balance")
    public BalanceResponse getBalance(@RequestParam("user_id") String user_id) {
        return walletService.getBalance(user_id);
    }

    @PostMapping(value = "/bank_info", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public BankInfo configureBankInfo(@RequestBody BankInfo bankInfo) {
        return walletService.configureBankInfo(bankInfo);
    }

    @PostMapping("/transactions")
    public Transactions walletTransactions(@RequestBody Transactions transactions) {
        if(Objects.isNull(transactions.getAmount()) || Objects.isNull(transactions.getUserId())) {
            throw new ValidationException("amount and userId must not be null");
        }
        return walletService.processTransaction(transactions);
    }

    @PostMapping("/payments")
    public void walletPayments() {

    }
}
