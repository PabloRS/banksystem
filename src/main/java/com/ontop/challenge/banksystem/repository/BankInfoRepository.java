package com.ontop.challenge.banksystem.repository;

import com.ontop.challenge.banksystem.model.BankInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankInfoRepository extends MongoRepository<BankInfo, String> {

    Optional<BankInfo> findByUserIdAndAccountNumber(String userId, String accountNumber);

    Optional<BankInfo> findByUserId(String userId);
}
