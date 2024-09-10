package com.example.zipcodeimporter;

import com.opencsv.CSVReader;
import com.opencsv.*;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
public class ZipCodeImporterService {

    @Autowired
    private ZipCodeRepository zipCodeRepository;

    public void importZipCodes(String csvFilePath) throws IOException, CsvException {
        System.out.println("Starting the import process...");

        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            List<String[]> rows = reader.readAll();
            int totalRows = rows.size() - 1;  // Subtract 1 for header
            int insertedCount = 0;
            int duplicateCount = 0;

            System.out.println("Reading and importing data from '" + csvFilePath + "'...");

            for (int i = 1; i < rows.size(); i++) {  // Start from 1 to skip header
                String[] row = rows.get(i);
                if (row.length > 0) {
                    String zipCode = row[0];
                    if (!zipCodeRepository.existsByCode(zipCode)) {
                        zipCodeRepository.save(new ZipCode(zipCode));
                        insertedCount++;
                    } else {
                        duplicateCount++;
                    }
                }

                if (i % 1000 == 0 || i == totalRows) {
                    System.out.println("Processed " + i + "/" + totalRows + " rows...");
                }
            }

            System.out.println("\nImport completed successfully.");
            System.out.println("Total rows processed: " + totalRows);
            System.out.println("Inserted: " + insertedCount);
            System.out.println("Duplicates skipped: " + duplicateCount);
        }
    }
}