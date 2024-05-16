package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Book;

public class BookManager {
	
	 private static final String FILE_PATH = "C:\\pravdaBooksBillManagement\\books.txt";
	    
	    public static List<Book> readBooksFromFile() {
	        List<Book> books = new ArrayList<>();
	        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	                try {
	                    String[] data = line.split(",");
	                    if (data.length < 4) {
	                        // Skip lines that do not have enough data
	                        continue;
	                    }
	                    int no = Integer.parseInt(data[0].trim());
	                    String name = data[1].trim();
	                    double rate = Double.parseDouble(data[2].trim());
	                    int quantity = Integer.parseInt(data[3].trim());
	                    double total = rate * quantity;
	                    books.add(new Book(no, name, rate, quantity, total));
	                } catch (NumberFormatException e) {
	                    System.err.println("Skipping line due to invalid number format: " + line);
	                    // Log this exception or handle it as needed
	                }
	            }
	        } catch (FileNotFoundException e) {
	            System.err.println("The file was not found: " + FILE_PATH);
	            e.printStackTrace();
	        } catch (IOException e) {
	            System.err.println("An error occurred when reading from the file.");
	            e.printStackTrace();
	        }
	        return books;
	    }
	}

