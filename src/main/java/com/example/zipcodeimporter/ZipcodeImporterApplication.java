package com.example.zipcodeimporter;

import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@SpringBootApplication
public class ZipcodeImporterApplication implements CommandLineRunner {

	@Autowired
	private ZipCodeImporterService zipCodeImporterService;

	@Value("${spring.data.mongodb.uri}")
	private String mongoUri;

	private static final String CSV_FILE_NAME = "zipcodesreduced.csv";
	private static final String LOG_FILE_NAME = "ingestion.log";

	public static void main(String[] args) {
		SpringApplication.run(ZipcodeImporterApplication.class, args);
	}

	@Override
	public void run(String... args) {
		try (PrintWriter logWriter = new PrintWriter(new FileWriter(LOG_FILE_NAME, true))) {
			logWriter.println("Starting the zip code import script at " + LocalDateTime.now());
			logWriter.println("MongoDB URI: " + maskPassword(mongoUri));
			logWriter.println("CSV File Name: " + CSV_FILE_NAME);

			String csvFilePath = findCsvFile();
			if (csvFilePath == null) {
				logWriter.println("CSV file not found. Please ensure 'zipcodesreduced.csv' is in the project root or src/main/resources directory.");
				return;
			}

			logWriter.println("CSV File Path: " + csvFilePath);

			try {
				zipCodeImporterService.importZipCodes(csvFilePath);
			} catch (IOException | CsvException e) {
				logWriter.println("Error occurred during import: " + e.getMessage());
				e.printStackTrace(logWriter);
			}

			logWriter.println("Script execution completed at " + LocalDateTime.now());
		} catch (IOException e) {
			System.err.println("Error writing to log file: " + e.getMessage());
		}
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

	private String maskPassword(String uri) {
		return uri.replaceAll("(:.*@)", ":****@");
	}
}