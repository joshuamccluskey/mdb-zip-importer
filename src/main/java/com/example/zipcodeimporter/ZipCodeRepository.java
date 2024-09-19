package com.example.zipcodeimporter;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ZipCodeRepository extends MongoRepository<ZipCode, String> {
    boolean existsByZip(String zip);
}