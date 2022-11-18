package com.techelevator;

import junit.framework.TestCase;
import org.junit.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VendingMachineCLITest extends TestCase {

    private static final String TEST_TEXT_FILE = "src/test/java/com/techelevator/resources/ObjectLoaderTestFile.csv";


    private static final String OUT_FILE_PATH = "out.txt";

    @AfterClass
    public void afterClass() throws Exception {
        File outFile = new File(OUT_FILE_PATH);
        if(outFile.exists()) {
            outFile.delete();
        }
    }


    // This method will loop through all tests for the VendingMachineCLI and return which test failed last or if all passed.
    @Test
    public void testMain() throws IOException {
    String message = "";

    // Test A
        Boolean testDisplayOptionsPassed = testDisplayOptions();
        if (!testDisplayOptionsPassed) { message = "[Error] Error encountered printing the current stock."; }

    // Test B
        Boolean testFeedMoneyPassed = testFeedMoney();
        if (!testFeedMoneyPassed) { message = "[Error] Error encountered feeding money into the machine."; }

    // Test C
        Boolean testSelectProductsPassed = testSelectProducts();
        if (!testSelectProductsPassed) { message = "[Error] Error encountered selecting products."; }

    // Test D
        Boolean testGenerateSalesReportPassed = testGenerateSalesReport();
        if (!testGenerateSalesReportPassed) { message = "[Error] Error encountered generating the sales report."; }

        // If all above tests passed the full test passes. If not the message will help guide the user to the last test that failed
        Assert.assertTrue(message, testDisplayOptionsPassed && testFeedMoneyPassed && testSelectProductsPassed && testGenerateSalesReportPassed);

    }


    //** TEST A **
    // This method tests that the display options are printing as expected.
    public Boolean testDisplayOptions() throws IOException {
        boolean testPassed;
        try{
            runTest("1");
        } catch(Exception e) {
            // Eat the exception
        } finally
         {
            testPassed = checkTestA();
        }
        return testPassed;
    }


    //** TEST B **
    // This method tests that funds are added and displayed correctly
    public Boolean testFeedMoney() throws IOException {
        boolean testPassed;
        try{
            runTest("2|1|1|r");
        } catch(Exception e) {
            // Eat the exception
        } finally
        {
            testPassed = checkTestB();
        }
        return testPassed;
    }


    //** TEST C **
    // This test checks that selectProducts are removed and displayed correctly.
    public Boolean testSelectProducts() throws IOException {
        boolean testPassed;
        try{
            runTest("2|1|5|r|2|d4|d4|d4|d4|d4|d4");
        } catch(Exception e) {
            // Eat the exception
        } finally
        {
            testPassed = checkTestC();
        }
        return testPassed;
    }


    //** TEST D **
    // This test checks that the sales report correctly generates.
    public Boolean testGenerateSalesReport() throws IOException {
        boolean testPassed;
        try{
            runTest("2|1|10|r|2|d2|b3|c2|r|3|4");
        } catch(Exception e) {
            // Eat the exception
        } finally
        {
            testPassed = checkTestD();
        }
        return testPassed;
    }


    // Test runner, takes actions as a string variable
    public void runTest(String input) throws IOException{
        String userInput = "";

        String[] inputArray = input.split("\\|");

        if (inputArray.length <= 1) {
            userInput = inputArray[0];
        } else {
            for (int i = 0; i < inputArray.length; i++) {
                userInput += inputArray[i] + "\n";
            }
        }
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));

        // set out to a txt file so we can write the results of the application
        File outFile = new File(OUT_FILE_PATH);
        PrintStream printStream = new PrintStream(outFile);
        System.setOut(printStream);
        VendingMachineCLI.main(null);

        List<String> lines = Files.readAllLines(outFile.toPath());
        File file = new File(OUT_FILE_PATH);

        String data = "";

        try(PrintWriter writer = new PrintWriter(new FileOutputStream(file.getAbsoluteFile(), file.exists()))){
            data = (file.length()>0)?"\n"+data:data;
            writer.append(data);
        }
        catch (Exception ex){
            ex.getStackTrace();
        }
        printStream.close();
    }


    // Test A
    public boolean checkTestA() throws IOException{
        String expected = "WELCOME THE VENDING MACHINE OF THE FUTURE!!!\n" +
                "\n" +
                "1) Display Vending Machine Items\n" +
                "2) Purchase\n" +
                "3) Exit\n" +
                "\n" +
                "Please choose an option >>> CURRENT STOCK IS: \n" +
                "A1: Potato Crisps, price= $3.05, amount left:5\n" +
                "A2: Stackers, price= $1.45, amount left:5\n" +
                "A3: Grain Waves, price= $2.75, amount left:5\n" +
                "A4: Cloud Popcorn, price= $3.65, amount left:5\n" +
                "B1: Moonpie, price= $1.80, amount left:5\n" +
                "B2: Cowtales, price= $1.50, amount left:5\n" +
                "B3: Wonka Bar, price= $1.50, amount left:5\n" +
                "B4: Crunchie, price= $1.75, amount left:5\n" +
                "C1: Cola, price= $1.25, amount left:5\n" +
                "C2: Dr. Salt, price= $1.50, amount left:5\n" +
                "C3: Mountain Melter, price= $1.50, amount left:5\n" +
                "C4: Heavy, price= $1.50, amount left:5\n" +
                "D1: U-Chews, price= $0.85, amount left:5\n" +
                "D2: Little League Chew, price= $0.95, amount left:5\n" +
                "D3: Chiclets, price= $0.75, amount left:5\n" +
                "D4: Triplemint, price= $0.75, amount left:5\n" +
                "\n" +
                "1) Display Vending Machine Items\n" +
                "2) Purchase\n" +
                "3) Exit\n" +
                "\n" +
                "Please choose an option >>> \n";


        String actual = "";

        File outFile = new File(OUT_FILE_PATH);

        List<String> lines = Files.readAllLines(outFile.toPath());

        for (String line : lines) {
            actual += line + "\n";
        }

        boolean isTrue;
        if (expected.equals(actual)) {
            return true;
        }
        return false;
    }


    // Test B
    // This test checks that money is properly added and displayed correctly.
    public boolean checkTestB() throws IOException{
        String expected = "WELCOME THE VENDING MACHINE OF THE FUTURE!!!\n" +
                "\n" +
                "1) Display Vending Machine Items\n" +
                "2) Purchase\n" +
                "3) Exit\n" +
                "\n" +
                "Please choose an option >>> \n" +
                "--------------------------\n" +
                "| Current balance: $0.00 |\n" +
                "--------------------------\n" +
                "\n" +
                "1) Feed Money\n" +
                "2) Select Product\n" +
                "3) Finish Transaction\n" +
                "\n" +
                "Please choose an option >>> \n" +
                "*********************************************\n" +
                "Current balance: $0.00\n" +
                "How much money would you like to add? (Enter a number or (R) to return: \n" +
                "*********************************************\n" +
                "Current balance: $1.00\n" +
                "How much money would you like to add? (Enter a number or (R) to return: \n" +
                "--------------------------\n" +
                "| Current balance: $1.00 |\n" +
                "--------------------------\n" +
                "\n" +
                "1) Feed Money\n" +
                "2) Select Product\n" +
                "3) Finish Transaction\n" +
                "\n" +
                "Please choose an option >>> \n";

        String actual = "";

        File outFile = new File(OUT_FILE_PATH);

        List<String> lines = Files.readAllLines(outFile.toPath());

        for (String line : lines) {
            actual += line + "\n";
        }

        boolean isTrue;
        if (expected.equals(actual)) {
            return true;
        }
        return false;
    }


    // Test C
    // This test checks that selectProducts are removed and displayed correctly.
    public boolean checkTestC() throws IOException{
        String expected = "WELCOME THE VENDING MACHINE OF THE FUTURE!!!\n" +
                "\n" +
                "1) Display Vending Machine Items\n" +
                "2) Purchase\n" +
                "3) Exit\n" +
                "\n" +
                "Please choose an option >>> \n" +
                "--------------------------\n" +
                "| Current balance: $0.00 |\n" +
                "--------------------------\n" +
                "\n" +
                "1) Feed Money\n" +
                "2) Select Product\n" +
                "3) Finish Transaction\n" +
                "\n" +
                "Please choose an option >>> \n" +
                "*********************************************\n" +
                "Current balance: $0.00\n" +
                "How much money would you like to add? (Enter a number or (R) to return: \n" +
                "*********************************************\n" +
                "Current balance: $5.00\n" +
                "How much money would you like to add? (Enter a number or (R) to return: \n" +
                "--------------------------\n" +
                "| Current balance: $5.00 |\n" +
                "--------------------------\n" +
                "\n" +
                "1) Feed Money\n" +
                "2) Select Product\n" +
                "3) Finish Transaction\n" +
                "\n" +
                "Please choose an option >>> CURRENT STOCK IS: \n" +
                "A1: Potato Crisps, price= $3.05, amount left:5\n" +
                "A2: Stackers, price= $1.45, amount left:5\n" +
                "A3: Grain Waves, price= $2.75, amount left:5\n" +
                "A4: Cloud Popcorn, price= $3.65, amount left:5\n" +
                "B1: Moonpie, price= $1.80, amount left:5\n" +
                "B2: Cowtales, price= $1.50, amount left:5\n" +
                "B3: Wonka Bar, price= $1.50, amount left:5\n" +
                "B4: Crunchie, price= $1.75, amount left:5\n" +
                "C1: Cola, price= $1.25, amount left:5\n" +
                "C2: Dr. Salt, price= $1.50, amount left:5\n" +
                "C3: Mountain Melter, price= $1.50, amount left:5\n" +
                "C4: Heavy, price= $1.50, amount left:5\n" +
                "D1: U-Chews, price= $0.85, amount left:5\n" +
                "D2: Little League Chew, price= $0.95, amount left:5\n" +
                "D3: Chiclets, price= $0.75, amount left:5\n" +
                "D4: Triplemint, price= $0.75, amount left:5\n" +
                "--------------------------\n" +
                "| Current balance: $5.00 |\n" +
                "--------------------------\n" +
                "\n" +
                "Enter the SlotID for the product you would like to purchase or press (R) to return: \n" +
                "\n" +
                "*********************************************!!! DISPENSING ITEM !!!*********************************************\n" +
                "\n" +
                "\n" +
                "Triplemint, price= $0.75, new balance: $4.25\n" +
                "Chew Chew, Yum!\n" +
                "CURRENT STOCK IS: \n" +
                "A1: Potato Crisps, price= $3.05, amount left:5\n" +
                "A2: Stackers, price= $1.45, amount left:5\n" +
                "A3: Grain Waves, price= $2.75, amount left:5\n" +
                "A4: Cloud Popcorn, price= $3.65, amount left:5\n" +
                "B1: Moonpie, price= $1.80, amount left:5\n" +
                "B2: Cowtales, price= $1.50, amount left:5\n" +
                "B3: Wonka Bar, price= $1.50, amount left:5\n" +
                "B4: Crunchie, price= $1.75, amount left:5\n" +
                "C1: Cola, price= $1.25, amount left:5\n" +
                "C2: Dr. Salt, price= $1.50, amount left:5\n" +
                "C3: Mountain Melter, price= $1.50, amount left:5\n" +
                "C4: Heavy, price= $1.50, amount left:5\n" +
                "D1: U-Chews, price= $0.85, amount left:5\n" +
                "D2: Little League Chew, price= $0.95, amount left:5\n" +
                "D3: Chiclets, price= $0.75, amount left:5\n" +
                "D4: Triplemint, price= $0.75, amount left:4\n" +
                "--------------------------\n" +
                "| Current balance: $4.25 |\n" +
                "--------------------------\n" +
                "\n" +
                "Enter the SlotID for the product you would like to purchase or press (R) to return: \n" +
                "\n" +
                "*********************************************!!! DISPENSING ITEM !!!*********************************************\n" +
                "\n" +
                "\n" +
                "Triplemint, price= $0.75, new balance: $3.50\n" +
                "Chew Chew, Yum!\n" +
                "CURRENT STOCK IS: \n" +
                "A1: Potato Crisps, price= $3.05, amount left:5\n" +
                "A2: Stackers, price= $1.45, amount left:5\n" +
                "A3: Grain Waves, price= $2.75, amount left:5\n" +
                "A4: Cloud Popcorn, price= $3.65, amount left:5\n" +
                "B1: Moonpie, price= $1.80, amount left:5\n" +
                "B2: Cowtales, price= $1.50, amount left:5\n" +
                "B3: Wonka Bar, price= $1.50, amount left:5\n" +
                "B4: Crunchie, price= $1.75, amount left:5\n" +
                "C1: Cola, price= $1.25, amount left:5\n" +
                "C2: Dr. Salt, price= $1.50, amount left:5\n" +
                "C3: Mountain Melter, price= $1.50, amount left:5\n" +
                "C4: Heavy, price= $1.50, amount left:5\n" +
                "D1: U-Chews, price= $0.85, amount left:5\n" +
                "D2: Little League Chew, price= $0.95, amount left:5\n" +
                "D3: Chiclets, price= $0.75, amount left:5\n" +
                "D4: Triplemint, price= $0.75, amount left:3\n" +
                "--------------------------\n" +
                "| Current balance: $3.50 |\n" +
                "--------------------------\n" +
                "\n" +
                "Enter the SlotID for the product you would like to purchase or press (R) to return: \n" +
                "\n" +
                "*********************************************!!! DISPENSING ITEM !!!*********************************************\n" +
                "\n" +
                "\n" +
                "Triplemint, price= $0.75, new balance: $2.75\n" +
                "Chew Chew, Yum!\n" +
                "CURRENT STOCK IS: \n" +
                "A1: Potato Crisps, price= $3.05, amount left:5\n" +
                "A2: Stackers, price= $1.45, amount left:5\n" +
                "A3: Grain Waves, price= $2.75, amount left:5\n" +
                "A4: Cloud Popcorn, price= $3.65, amount left:5\n" +
                "B1: Moonpie, price= $1.80, amount left:5\n" +
                "B2: Cowtales, price= $1.50, amount left:5\n" +
                "B3: Wonka Bar, price= $1.50, amount left:5\n" +
                "B4: Crunchie, price= $1.75, amount left:5\n" +
                "C1: Cola, price= $1.25, amount left:5\n" +
                "C2: Dr. Salt, price= $1.50, amount left:5\n" +
                "C3: Mountain Melter, price= $1.50, amount left:5\n" +
                "C4: Heavy, price= $1.50, amount left:5\n" +
                "D1: U-Chews, price= $0.85, amount left:5\n" +
                "D2: Little League Chew, price= $0.95, amount left:5\n" +
                "D3: Chiclets, price= $0.75, amount left:5\n" +
                "D4: Triplemint, price= $0.75, amount left:2\n" +
                "--------------------------\n" +
                "| Current balance: $2.75 |\n" +
                "--------------------------\n" +
                "\n" +
                "Enter the SlotID for the product you would like to purchase or press (R) to return: \n" +
                "\n" +
                "*********************************************!!! DISPENSING ITEM !!!*********************************************\n" +
                "\n" +
                "\n" +
                "Triplemint, price= $0.75, new balance: $2.00\n" +
                "Chew Chew, Yum!\n" +
                "CURRENT STOCK IS: \n" +
                "A1: Potato Crisps, price= $3.05, amount left:5\n" +
                "A2: Stackers, price= $1.45, amount left:5\n" +
                "A3: Grain Waves, price= $2.75, amount left:5\n" +
                "A4: Cloud Popcorn, price= $3.65, amount left:5\n" +
                "B1: Moonpie, price= $1.80, amount left:5\n" +
                "B2: Cowtales, price= $1.50, amount left:5\n" +
                "B3: Wonka Bar, price= $1.50, amount left:5\n" +
                "B4: Crunchie, price= $1.75, amount left:5\n" +
                "C1: Cola, price= $1.25, amount left:5\n" +
                "C2: Dr. Salt, price= $1.50, amount left:5\n" +
                "C3: Mountain Melter, price= $1.50, amount left:5\n" +
                "C4: Heavy, price= $1.50, amount left:5\n" +
                "D1: U-Chews, price= $0.85, amount left:5\n" +
                "D2: Little League Chew, price= $0.95, amount left:5\n" +
                "D3: Chiclets, price= $0.75, amount left:5\n" +
                "D4: Triplemint, price= $0.75, amount left:1\n" +
                "--------------------------\n" +
                "| Current balance: $2.00 |\n" +
                "--------------------------\n" +
                "\n" +
                "Enter the SlotID for the product you would like to purchase or press (R) to return: \n" +
                "\n" +
                "*********************************************!!! DISPENSING ITEM !!!*********************************************\n" +
                "\n" +
                "\n" +
                "Triplemint, price= $0.75, new balance: $1.25\n" +
                "Chew Chew, Yum!\n" +
                "CURRENT STOCK IS: \n" +
                "A1: Potato Crisps, price= $3.05, amount left:5\n" +
                "A2: Stackers, price= $1.45, amount left:5\n" +
                "A3: Grain Waves, price= $2.75, amount left:5\n" +
                "A4: Cloud Popcorn, price= $3.65, amount left:5\n" +
                "B1: Moonpie, price= $1.80, amount left:5\n" +
                "B2: Cowtales, price= $1.50, amount left:5\n" +
                "B3: Wonka Bar, price= $1.50, amount left:5\n" +
                "B4: Crunchie, price= $1.75, amount left:5\n" +
                "C1: Cola, price= $1.25, amount left:5\n" +
                "C2: Dr. Salt, price= $1.50, amount left:5\n" +
                "C3: Mountain Melter, price= $1.50, amount left:5\n" +
                "C4: Heavy, price= $1.50, amount left:5\n" +
                "D1: U-Chews, price= $0.85, amount left:5\n" +
                "D2: Little League Chew, price= $0.95, amount left:5\n" +
                "D3: Chiclets, price= $0.75, amount left:5\n" +
                "D4: Triplemint, price= $0.75, SOLD OUT\n" +
                "--------------------------\n" +
                "| Current balance: $1.25 |\n" +
                "--------------------------\n" +
                "\n" +
                "Enter the SlotID for the product you would like to purchase or press (R) to return: Sorry, we are all sold out of Triplemint\n" +
                "CURRENT STOCK IS: \n" +
                "A1: Potato Crisps, price= $3.05, amount left:5\n" +
                "A2: Stackers, price= $1.45, amount left:5\n" +
                "A3: Grain Waves, price= $2.75, amount left:5\n" +
                "A4: Cloud Popcorn, price= $3.65, amount left:5\n" +
                "B1: Moonpie, price= $1.80, amount left:5\n" +
                "B2: Cowtales, price= $1.50, amount left:5\n" +
                "B3: Wonka Bar, price= $1.50, amount left:5\n" +
                "B4: Crunchie, price= $1.75, amount left:5\n" +
                "C1: Cola, price= $1.25, amount left:5\n" +
                "C2: Dr. Salt, price= $1.50, amount left:5\n" +
                "C3: Mountain Melter, price= $1.50, amount left:5\n" +
                "C4: Heavy, price= $1.50, amount left:5\n" +
                "D1: U-Chews, price= $0.85, amount left:5\n" +
                "D2: Little League Chew, price= $0.95, amount left:5\n" +
                "D3: Chiclets, price= $0.75, amount left:5\n" +
                "D4: Triplemint, price= $0.75, SOLD OUT\n" +
                "--------------------------\n" +
                "| Current balance: $1.25 |\n" +
                "--------------------------\n" +
                "\n" +
                "Enter the SlotID for the product you would like to purchase or press (R) to return: \n";

        String actual = "";

        File outFile = new File(OUT_FILE_PATH);

        List<String> lines = Files.readAllLines(outFile.toPath());

        for (String line : lines) {
            actual += line + "\n";
        }

        boolean isTrue;
        if (expected.equals(actual)) {
            return true;
        }
        return false;
    }


    //** TEST D **
    // This test checks that the sales report correctly generates.
    public boolean checkTestD() throws IOException{
        String expected = "WELCOME THE VENDING MACHINE OF THE FUTURE!!!\n" +
                "\n" +
                "1) Display Vending Machine Items\n" +
                "2) Purchase\n" +
                "3) Exit\n" +
                "\n" +
                "Please choose an option >>> \n" +
                "--------------------------\n" +
                "| Current balance: $0.00 |\n" +
                "--------------------------\n" +
                "\n" +
                "1) Feed Money\n" +
                "2) Select Product\n" +
                "3) Finish Transaction\n" +
                "\n" +
                "Please choose an option >>> \n" +
                "*********************************************\n" +
                "Current balance: $0.00\n" +
                "How much money would you like to add? (Enter a number or (R) to return: \n" +
                "*********************************************\n" +
                "Current balance: $10.00\n" +
                "How much money would you like to add? (Enter a number or (R) to return: \n" +
                "--------------------------\n" +
                "| Current balance: $10.00 |\n" +
                "--------------------------\n" +
                "\n" +
                "1) Feed Money\n" +
                "2) Select Product\n" +
                "3) Finish Transaction\n" +
                "\n" +
                "Please choose an option >>> CURRENT STOCK IS: \n" +
                "A1: Potato Crisps, price= $3.05, amount left:5\n" +
                "A2: Stackers, price= $1.45, amount left:5\n" +
                "A3: Grain Waves, price= $2.75, amount left:5\n" +
                "A4: Cloud Popcorn, price= $3.65, amount left:5\n" +
                "B1: Moonpie, price= $1.80, amount left:5\n" +
                "B2: Cowtales, price= $1.50, amount left:5\n" +
                "B3: Wonka Bar, price= $1.50, amount left:5\n" +
                "B4: Crunchie, price= $1.75, amount left:5\n" +
                "C1: Cola, price= $1.25, amount left:5\n" +
                "C2: Dr. Salt, price= $1.50, amount left:5\n" +
                "C3: Mountain Melter, price= $1.50, amount left:5\n" +
                "C4: Heavy, price= $1.50, amount left:5\n" +
                "D1: U-Chews, price= $0.85, amount left:5\n" +
                "D2: Little League Chew, price= $0.95, amount left:5\n" +
                "D3: Chiclets, price= $0.75, amount left:5\n" +
                "D4: Triplemint, price= $0.75, amount left:5\n" +
                "--------------------------\n" +
                "| Current balance: $10.00 |\n" +
                "--------------------------\n" +
                "\n" +
                "Enter the SlotID for the product you would like to purchase or press (R) to return: \n" +
                "\n" +
                "*********************************************!!! DISPENSING ITEM !!!*********************************************\n" +
                "\n" +
                "\n" +
                "Little League Chew, price= $0.95, new balance: $9.05\n" +
                "Chew Chew, Yum!\n" +
                "CURRENT STOCK IS: \n" +
                "A1: Potato Crisps, price= $3.05, amount left:5\n" +
                "A2: Stackers, price= $1.45, amount left:5\n" +
                "A3: Grain Waves, price= $2.75, amount left:5\n" +
                "A4: Cloud Popcorn, price= $3.65, amount left:5\n" +
                "B1: Moonpie, price= $1.80, amount left:5\n" +
                "B2: Cowtales, price= $1.50, amount left:5\n" +
                "B3: Wonka Bar, price= $1.50, amount left:5\n" +
                "B4: Crunchie, price= $1.75, amount left:5\n" +
                "C1: Cola, price= $1.25, amount left:5\n" +
                "C2: Dr. Salt, price= $1.50, amount left:5\n" +
                "C3: Mountain Melter, price= $1.50, amount left:5\n" +
                "C4: Heavy, price= $1.50, amount left:5\n" +
                "D1: U-Chews, price= $0.85, amount left:5\n" +
                "D2: Little League Chew, price= $0.95, amount left:4\n" +
                "D3: Chiclets, price= $0.75, amount left:5\n" +
                "D4: Triplemint, price= $0.75, amount left:5\n" +
                "--------------------------\n" +
                "| Current balance: $9.05 |\n" +
                "--------------------------\n" +
                "\n" +
                "Enter the SlotID for the product you would like to purchase or press (R) to return: \n" +
                "\n" +
                "*********************************************!!! DISPENSING ITEM !!!*********************************************\n" +
                "\n" +
                "\n" +
                "Wonka Bar, price= $1.50, new balance: $7.55\n" +
                "Munch Munch, Yum!\n" +
                "CURRENT STOCK IS: \n" +
                "A1: Potato Crisps, price= $3.05, amount left:5\n" +
                "A2: Stackers, price= $1.45, amount left:5\n" +
                "A3: Grain Waves, price= $2.75, amount left:5\n" +
                "A4: Cloud Popcorn, price= $3.65, amount left:5\n" +
                "B1: Moonpie, price= $1.80, amount left:5\n" +
                "B2: Cowtales, price= $1.50, amount left:5\n" +
                "B3: Wonka Bar, price= $1.50, amount left:4\n" +
                "B4: Crunchie, price= $1.75, amount left:5\n" +
                "C1: Cola, price= $1.25, amount left:5\n" +
                "C2: Dr. Salt, price= $1.50, amount left:5\n" +
                "C3: Mountain Melter, price= $1.50, amount left:5\n" +
                "C4: Heavy, price= $1.50, amount left:5\n" +
                "D1: U-Chews, price= $0.85, amount left:5\n" +
                "D2: Little League Chew, price= $0.95, amount left:4\n" +
                "D3: Chiclets, price= $0.75, amount left:5\n" +
                "D4: Triplemint, price= $0.75, amount left:5\n" +
                "--------------------------\n" +
                "| Current balance: $7.55 |\n" +
                "--------------------------\n" +
                "\n" +
                "Enter the SlotID for the product you would like to purchase or press (R) to return: \n" +
                "\n" +
                "*********************************************!!! DISPENSING ITEM !!!*********************************************\n" +
                "\n" +
                "\n" +
                "Dr. Salt, price= $1.50, new balance: $6.05\n" +
                "Glug Glug, Yum!\n" +
                "CURRENT STOCK IS: \n" +
                "A1: Potato Crisps, price= $3.05, amount left:5\n" +
                "A2: Stackers, price= $1.45, amount left:5\n" +
                "A3: Grain Waves, price= $2.75, amount left:5\n" +
                "A4: Cloud Popcorn, price= $3.65, amount left:5\n" +
                "B1: Moonpie, price= $1.80, amount left:5\n" +
                "B2: Cowtales, price= $1.50, amount left:5\n" +
                "B3: Wonka Bar, price= $1.50, amount left:4\n" +
                "B4: Crunchie, price= $1.75, amount left:5\n" +
                "C1: Cola, price= $1.25, amount left:5\n" +
                "C2: Dr. Salt, price= $1.50, amount left:4\n" +
                "C3: Mountain Melter, price= $1.50, amount left:5\n" +
                "C4: Heavy, price= $1.50, amount left:5\n" +
                "D1: U-Chews, price= $0.85, amount left:5\n" +
                "D2: Little League Chew, price= $0.95, amount left:4\n" +
                "D3: Chiclets, price= $0.75, amount left:5\n" +
                "D4: Triplemint, price= $0.75, amount left:5\n" +
                "--------------------------\n" +
                "| Current balance: $6.05 |\n" +
                "--------------------------\n" +
                "\n" +
                "Enter the SlotID for the product you would like to purchase or press (R) to return: Test spot\n" +
                "\n" +
                "--------------------------\n" +
                "| Current balance: $6.05 |\n" +
                "--------------------------\n" +
                "\n" +
                "1) Feed Money\n" +
                "2) Select Product\n" +
                "3) Finish Transaction\n" +
                "\n" +
                "Please choose an option >>> \n" +
                "\n" +
                "*********************************************$$$ DISPENSING $6.05 in CHANGE $$$*********************************************\n" +
                "\n" +
                "\n" +
                "Dispensing 24 quarters, 0 dimes, and 1 nickels\n" +
                "WELCOME THE VENDING MACHINE OF THE FUTURE!!!\n" +
                "\n" +
                "1) Display Vending Machine Items\n" +
                "2) Purchase\n" +
                "3) Exit\n" +
                "\n" +
                "Please choose an option >>> Sales Report created\n" +
                "\n" +
                "1) Display Vending Machine Items\n" +
                "2) Purchase\n" +
                "3) Exit\n" +
                "\n" +
                "Please choose an option >>> \n";


        String actual = "";

        File outFile = new File(OUT_FILE_PATH);

        List<String> lines = Files.readAllLines(outFile.toPath());

        for (String line : lines) {
            actual += line + "\n";
        }

        boolean isTrue;
        if (expected.equals(actual)) {
            return true;
        }
        return false;
    }




    /*
     * Adds a new line feed after each input
     */
    private String concatWithNewLineFeed(String ...inputs) {
        String userInput = "";
        for(String s : inputs) {
            userInput += s + System.lineSeparator();
        }
        return userInput;
    }
}