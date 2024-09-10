package com.example.zipcodeimporter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "zipcodes")
public class ZipCode {
    @Id
    private String id;
    private String code;

    public ZipCode(String code) {
        this.code = code;
    }
}