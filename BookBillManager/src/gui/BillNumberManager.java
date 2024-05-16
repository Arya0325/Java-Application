package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class BillNumberManager {
	 private static final String BILL_NUMBER_FILE = "lastbillnumber.txt";

	    public static int getLastBillNumber() {
	        File file = new File(BILL_NUMBER_FILE);
	        if (!file.exists()) {
	            return 0; // If the file doesn't exist, return 0 as the initial bill number
	        }

	        try (Scanner scanner = new Scanner(file)) {
	            return scanner.nextInt();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }

	        return 0; // Return 0 in case of any error
	    }

	    public static void saveLastBillNumber(int billNumber) {
	        try (FileWriter writer = new FileWriter(BILL_NUMBER_FILE)) {
	            writer.write(Integer.toString(billNumber));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    public static int getNextBillNumber() {
	        int lastBillNumber = getLastBillNumber();
	        int nextBillNumber = lastBillNumber + 1;
	        saveLastBillNumber(nextBillNumber); // Save the incremented bill number for future use
	        return nextBillNumber;
	    }
	}