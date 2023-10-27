package com.ontop.challenge.banksystem.service;

import com.ontop.challenge.banksystem.exceptionhandler.UserNotFoundException;
import com.ontop.challenge.banksystem.model.BalanceResponse;
import com.ontop.challenge.banksystem.model.BankInfo;
import com.ontop.challenge.banksystem.repository.BankInfoRepository;
import com.ontop.challenge.banksystem.repository.TransactionsRepository;
import com.ontop.challenge.banksystem.service.impl.WalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WalletServiceImplTest {

    private BankInfoRepository bankInfoRepository;
    private TransactionsRepository transactionsRepository;
    private WalletService walletService;

    @BeforeEach
    public void setUp() {
        bankInfoRepository = mock(BankInfoRepository.class);
        transactionsRepository = mock(TransactionsRepository.class);
        walletService = new WalletServiceImpl(bankInfoRepository, transactionsRepository);
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

    private BankInfo stubBankInfo() {
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

    private BankInfo stubBankInfoUpdate() {
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
}
