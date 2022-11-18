package com.techelevator;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.techelevator.utilities.Logger;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class LoggerTest {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    Logger logger;

    @Before
    public void setup() {logger = new Logger("testLog");}

    @Test
    public void does_log_create_and_message_in_file() {

        String expectedMessage = dateTimeFormatter.format(now) + " LogTest";

        logger.log("LogTest");
        File actual = new File("testLog.txt");
        String actualLineOfText = "";
        if (actual.exists())
            try(Scanner reader = new Scanner(actual)){
                actualLineOfText = reader.nextLine();
            }catch (Exception e){
                System.out.println("File not found or message");
            }
        Assert.assertTrue("File never created",actual.exists());
        Assert.assertEquals("Message is not as expected", expectedMessage,actualLineOfText);


    }

    @Test
    public void does_salesLog_create_and_message_in_file() {
        String expectedMessage = "LogTest";

        logger.salesLog("LogTest");
        File actual = new File("testLog.txt");
        String actualLineOfText = "";
        if (actual.exists())
            try(Scanner reader = new Scanner(actual)){
                actualLineOfText = reader.nextLine();
            }catch (Exception e){
                System.out.println("File not found or message");
            }
        Assert.assertTrue("File never created",actual.exists());
        Assert.assertEquals("Message is not as expected", expectedMessage,actualLineOfText);

    }
    @After
    public void cleanup(){
        File actual = new File("testLog.txt");
        if (actual.exists()){
            actual.delete();
        }
    }

    public void testSalesLog() {
    }
}