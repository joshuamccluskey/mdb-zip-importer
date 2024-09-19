package com.example.zipcodeimporter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document(collection = "zipcodes")
public class ZipCode {
    @Id
    private String zip;
    private String countyName;
    private String state;
    private String stCountyFp;
    private String classFp;

    public ZipCode(String zip, String countyName, String state, String stCountyFp, String classFp) {
        this.zip = zip;
        this.countyName = countyName;
        this.state = state;
        this.stCountyFp = stCountyFp;
        this.classFp = classFp;
    }
}