package com.ontop.challenge.banksystem.service;

import com.ontop.challenge.banksystem.exceptionhandler.TransactionNotValidException;
import com.ontop.challenge.banksystem.exceptionhandler.UserNotFoundException;
import com.ontop.challenge.banksystem.model.*;
import com.ontop.challenge.banksystem.repository.BankInfoRepository;
import com.ontop.challenge.banksystem.repository.TransactionsRepository;
import com.ontop.challenge.banksystem.service.impl.WalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static com.ontop.challenge.banksystem.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WalletServiceImplTest {

    private BankInfoRepository bankInfoRepository;
    private TransactionsRepository transactionsRepository;
    private WalletService walletService;
    private BigDecimal fee;

    @BeforeEach
    public void setUp() {
        this.bankInfoRepository = mock(BankInfoRepository.class);
        this.transactionsRepository = mock(TransactionsRepository.class);
        this.fee = new BigDecimal("10");
        this.walletService = new WalletServiceImpl(bankInfoRepository, transactionsRepository, fee);
    }

    @Test
    public void testBalanceUserExist() {
        String user_id = "123";
        when(bankInfoRepository.findByUserId(user_id)).thenReturn(Optional.of(stubBankInfo()));

        BalanceResponse response = walletService.getBalance(user_id);

        assertNotNull(response);
        assertEquals(user_id, response.getUser_id());
    }

    @Test
    public void testBalanceUserNotExist() {
        String user_id = "000";
        when(bankInfoRepository.findByUserId(user_id)).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> walletService.getBalance(user_id));
    }

    @Test
    public void testConfigureBankInfo() {
        BankInfo configureInfo = stubBankInfo();
        when(bankInfoRepository.save(configureInfo)).thenReturn(stubBankInfo());

        BankInfo response = walletService.configureBankInfo(configureInfo);
        assertNotNull(response);
        assertTrue(response.equals(configureInfo));
    }

    @Test
    public void testUpdateBankInfo() {
        BankInfo configureInfo = stubBankInfo();
        configureInfo.setBankName("santander");

        when(bankInfoRepository.findByUserIdAndAccountNumber(configureInfo.getUserId(), configureInfo.getAccountNumber()))
                .thenReturn(Optional.of(stubBankInfo()));

        when(bankInfoRepository.save(configureInfo)).thenReturn(stubBankInfoUpdate());

        BankInfo infoUpdated = walletService.configureBankInfo(configureInfo);

        assertNotNull(infoUpdated);
        assertTrue(infoUpdated.getBankName().equals("santander"));
    }

    @Test
    public void testTransactionUserNotFound() {
        String user_id = "000";
        Transactions transaction = new Transactions();
        transaction.setAmount(new BigDecimal("1000"));
        when(bankInfoRepository.findByUserId(user_id)).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> walletService.processTransaction(transaction));
    }

    @Test
    public void testTransactionInvalid() {
        String user_id = "123";
        Transactions transaction = new Transactions();
        transaction.setAmount(new BigDecimal("0"));
        transaction.setUserId(user_id);
        when(bankInfoRepository.findByUserId(user_id)).thenReturn(Optional.of(stubBankInfo()))
                .thenThrow(TransactionNotValidException.class);

        assertThrows(TransactionNotValidException.class, () -> walletService.processTransaction(transaction));
    }

    @Test
    @Disabled
    public void testTransactionTopup() {
        String user_id = "123";
        Transactions transaction = new Transactions();
        transaction.setAmount(new BigDecimal("5000"));
        transaction.setUserId(user_id);
        when(bankInfoRepository.findByUserId(user_id)).thenReturn(Optional.of(stubBankInfo()));
        TransactionRequest request = buildTransactionRequest(transaction);
        TransactionResponse response = buildTransactionResponse(request);
        Transactions updateTransaction = buildTransaction(response);
        when(transactionsRepository.save(updateTransaction)).thenReturn(stubTransaction("5000"));

        Transactions transactions = walletService.processTransaction(transaction);

        assertNotNull(transactions);
    }

    @Test
    @Disabled
    public void testTransactionWithdraw() {
        String user_id = "123";
        Transactions transaction = new Transactions();
        transaction.setAmount(new BigDecimal("-5000"));
        transaction.setUserId(user_id);
        when(bankInfoRepository.findByUserId(user_id)).thenReturn(Optional.of(stubBankInfo()));
        TransactionRequest request = buildTransactionRequest(transaction);
        TransactionResponse response = buildTransactionResponse(request);
        Transactions updateTransaction = buildTransaction(response);
        when(transactionsRepository.save(updateTransaction)).thenReturn(stubTransaction("-5000"));

        Transactions transactions = walletService.processTransaction(transaction);

        assertNotNull(transactions);
    }

}
