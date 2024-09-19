package com.example.zipcodeimporter;

import com.opencsv.CSVReader;
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
            int processedCount = 0;
            int updatedCount = 0;

            System.out.println("Reading and importing data from '" + csvFilePath + "'...");

            for (int i = 1; i < rows.size(); i++) {  // Start from 1 to skip header
                String[] row = rows.get(i);
                if (row.length == 5) {  // Ensure we have all 5 columns
                    String zip = row[0];
                    String countyName = row[1];
                    String state = row[2];
                    String stCountyFp = row[3];
                    String classFp = row[4];

                    ZipCode zipCodeEntity = new ZipCode(zip, countyName, state, stCountyFp, classFp);
                    zipCodeRepository.save(zipCodeEntity);
                    updatedCount++;
                }

                processedCount++;
                if (processedCount % 1000 == 0 || processedCount == totalRows) {
                    System.out.println("Processed " + processedCount + "/" + totalRows + " rows...");
                }
            }

            System.out.println("\nImport completed successfully.");
            System.out.println("Total rows processed: " + processedCount);
            System.out.println("Records updated/inserted: " + updatedCount);
        }
    }
}