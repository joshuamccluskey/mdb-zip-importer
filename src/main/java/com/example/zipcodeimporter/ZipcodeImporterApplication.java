package com.example.zipcodeimporter;

import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class ZipcodeImporterApplication implements CommandLineRunner {

	@Autowired
	private ZipCodeImporterService zipCodeImporterService;

	// CSV file name
	private static final String CSV_FILE_NAME = "zipcodesreduced.csv";

	public static void main(String[] args) {
		SpringApplication.run(ZipcodeImporterApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("Starting the zip code import script...");

		String csvFilePath = findCsvFile();
		if (csvFilePath == null) {
			System.err.println("CSV file not found. Please ensure 'zipcodesreduced.csv' is in the project root or src/main/resources directory.");
			return;
		}

		try {
			zipCodeImporterService.importZipCodes(csvFilePath);
		} catch (IOException | CsvException e) {
			System.err.println("Error occurred during import: " + e.getMessage());
			e.printStackTrace();
		}

		System.out.println("Script execution completed.");
	}

	private String findCsvFile() {
		// First, check in the project root (for GitHub Actions)
		File file = new File(CSV_FILE_NAME);
		if (file.exists()) {
			return file.getAbsolutePath();
		}

		// If not found, check in src/main/resources (for local development)
		file = new File("src/main/resources/" + CSV_FILE_NAME);
		if (file.exists()) {
			return file.getAbsolutePath();
		}

		// If still not found, return null
		return null;
	}
}