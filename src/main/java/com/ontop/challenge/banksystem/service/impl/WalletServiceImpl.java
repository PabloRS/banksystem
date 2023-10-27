package com.ontop.challenge.banksystem.service.impl;

import com.ontop.challenge.banksystem.exceptionhandler.TransactionNotValidException;
import com.ontop.challenge.banksystem.exceptionhandler.UserNotFoundException;
import com.ontop.challenge.banksystem.model.*;
import com.ontop.challenge.banksystem.repository.BankInfoRepository;
import com.ontop.challenge.banksystem.repository.TransactionsRepository;
import com.ontop.challenge.banksystem.service.WalletService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;

import static com.ontop.challenge.banksystem.constants.ConnectionURLs.BALANCE_URL;
import static com.ontop.challenge.banksystem.constants.ConnectionURLs.TRANSACTION_URL;
import static com.ontop.challenge.banksystem.constants.ExceptionsMessages.INVALID_TRANSACTION;
import static com.ontop.challenge.banksystem.constants.ExceptionsMessages.USER_NOT_FOUND;

@Service
public class WalletServiceImpl implements WalletService {

    private final BankInfoRepository bankInfoRepository;
    private final TransactionsRepository transactionsRepository;

    @Value("${wallet.transactions.fee}")
    private BigDecimal fee;

    RestTemplate restTemplate = new RestTemplate();

    public WalletServiceImpl(BankInfoRepository bankInfoRepository,
                             TransactionsRepository transactionsRepository) {
        this.bankInfoRepository = bankInfoRepository;
        this.transactionsRepository = transactionsRepository;
    }

    @Override
    public BalanceResponse getBalance(String user_id) {
        bankInfoRepository.findByUserId(user_id).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        BalanceResponse balanceResponse = restTemplate.getForObject(String.format("%s%s", BALANCE_URL, user_id), BalanceResponse.class);
        return balanceResponse;
    }

    @Override
    public BankInfo configureBankInfo(BankInfo bankInfo) {
        BankInfo response = bankInfoRepository.findByUserIdAndAccountNumber(bankInfo.getUserId(), bankInfo.getAccountNumber())
                .orElseGet(() -> bankInfoRepository.save(bankInfo));
        return updateBankInfo(response, bankInfo);
    }

    @Override
    public Transactions processTransaction(Transactions transaction) {
        bankInfoRepository.findByUserId(transaction.getUserId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        if(transaction.getAmount().intValue() == 0) {
            throw new TransactionNotValidException(INVALID_TRANSACTION);
        }
        TransactionRequest request = new TransactionRequest(String.valueOf(transaction.getAmount()), transaction.getUserId());
        TransactionResponse transactionResponse;
        if(transaction.getAmount().intValue() > 0) {
            transactionResponse = topupTransaction(request);
        } else {
            transactionResponse = withdrawTransaction(request);
        }
        return buildTransaction(transactionResponse);
    }

    private Transactions buildTransaction(TransactionResponse response) {
        Transactions transaction = new Transactions();
        transaction.setTransactionId(response.getWallet_transaction_id());
        transaction.setUserId(response.getUser_id());
        transaction.setAmount(applyFee(response.getAmount()));
        transaction.setStatus(Status.COMPLETED);
        transaction.setCreationDate(Instant.now());
        return transactionsRepository.save(transaction);
    }

    private BigDecimal applyFee(String amount) {
        BigDecimal originalValue = new BigDecimal(amount);
        BigDecimal increasedValue = originalValue.multiply(fee.divide(new BigDecimal("100")));
        return originalValue.add(increasedValue);
    }

    private TransactionResponse topupTransaction(TransactionRequest request) {
        TransactionResponse response = restTemplate.postForObject(TRANSACTION_URL, request, TransactionResponse.class);
        return response;
    }

    private TransactionResponse withdrawTransaction(TransactionRequest request) {
        TransactionResponse response = restTemplate.postForObject(TRANSACTION_URL, request, TransactionResponse.class);
        return response;
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
