package com.ontop.challenge.banksystem.utils;

import com.ontop.challenge.banksystem.model.*;

import java.math.BigDecimal;
import java.time.Instant;

public class TestUtils {

    private static BigDecimal fee = new BigDecimal("10");

    public static BankInfo stubBankInfo() {
        BankInfo bankInfo = new BankInfo();
        bankInfo.setUserId("123");
        bankInfo.setFirstName("Pablo");
        bankInfo.setLastName("Ramirez");
        bankInfo.setBankName("citi");
        bankInfo.setAccountNumber("1111");
        bankInfo.setNiNumber("3423");
        bankInfo.setRoutingNumber("92929");
        return bankInfo;
    }

    public static BankInfo stubBankInfoUpdate() {
        BankInfo bankInfo = new BankInfo();
        bankInfo.setUserId("123");
        bankInfo.setFirstName("Pablo");
        bankInfo.setLastName("Ramirez");
        bankInfo.setBankName("santander");
        bankInfo.setAccountNumber("1111");
        bankInfo.setNiNumber("3423");
        bankInfo.setRoutingNumber("92929");
        return bankInfo;
    }

    public static Transactions buildTransaction(TransactionResponse response) {
        Transactions transaction = new Transactions();
        transaction.setTransactionId(response.getWallet_transaction_id());
        transaction.setUserId(response.getUser_id());
        transaction.setAmount(applyFee(response.getAmount()));
        transaction.setStatus(Status.COMPLETED);
        transaction.setCreationDate(Instant.now());
        return transaction;
    }

    public static Transactions stubTransaction(String amount) {
        Transactions transaction = new Transactions();
        transaction.setTransactionId("121212");
        transaction.setUserId("123");
        transaction.setAmount(applyFee(amount));
        transaction.setStatus(Status.COMPLETED);
        transaction.setCreationDate(Instant.now());
        return transaction;
    }

    public static TransactionRequest buildTransactionRequest(Transactions transaction) {
        return new TransactionRequest(String.valueOf(transaction.getAmount()), transaction.getUserId());
    }

    public static TransactionResponse buildTransactionResponse(TransactionRequest request) {
        TransactionResponse response = new TransactionResponse();
        response.setAmount(request.getAmount());
        response.setUser_id(request.getUser_id());
        response.setWallet_transaction_id("121212");
        return response;
    }

    public static BigDecimal applyFee(String amount) {
        BigDecimal originalValue = new BigDecimal(amount);
        BigDecimal increasedValue = originalValue.multiply(fee.divide(new BigDecimal("100")));
        return originalValue.add(increasedValue);
    }
}
