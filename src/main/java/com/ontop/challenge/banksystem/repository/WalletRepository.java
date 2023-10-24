package com.ontop.challenge.banksystem.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface WalletRepository extends MongoRepository<Object, String> {
}
