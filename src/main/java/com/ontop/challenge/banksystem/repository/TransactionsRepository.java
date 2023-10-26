package com.ontop.challenge.banksystem.repository;

import com.ontop.challenge.banksystem.model.Transactions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsRepository extends MongoRepository<Transactions, String> {
}
