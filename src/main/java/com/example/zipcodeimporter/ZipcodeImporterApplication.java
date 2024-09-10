package com.example.zipcodeimporter;

import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ZipcodeImporterApplication implements CommandLineRunner {

	@Autowired
	private ZipCodeImporterService zipCodeImporterService;

	public static void main(String[] args) {
		SpringApplication.run(ZipcodeImporterApplication.class, args);
	}

	@Override
	public void run(String... args) {
		if (args.length < 1) {
			System.out.println("Please provide the CSV file path as a command-line argument.");
			return;
		}

		String csvFilePath = args[0];
		System.out.println("Starting the zip code import script...");

		try {
			zipCodeImporterService.importZipCodes(csvFilePath);
		} catch (IOException | CsvException e) {
			System.err.println("Error occurred during import: " + e.getMessage());
			e.printStackTrace();
		}

		System.out.println("Script execution completed.");
	}
}