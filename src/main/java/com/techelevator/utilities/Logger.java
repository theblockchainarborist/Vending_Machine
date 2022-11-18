package com.techelevator.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private String logFileName;
    private PrintWriter logWriter = null;


    public Logger(String logFileName) {
        this.logFileName = logFileName;
    }

    public void log(String message) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try (PrintWriter newWriter = new PrintWriter(new FileOutputStream(new File("Logs/" + logFileName + ".txt"), true))) {
            logWriter = newWriter;
            this.logWriter.append( dateTimeFormatter.format(now) + " " + message + "\n");
        } catch (Exception e) {
            System.out.println("Sorry file not found");
        }
    }

    //LocalTime.now().toString()

    public void salesLog(String message) {
        try (PrintWriter newWriter = new PrintWriter(new FileOutputStream(new File("SalesReports/" + logFileName + ".txt"), true))) {
            logWriter = newWriter;
            this.logWriter.append(message + "\n");
        } catch (Exception e) {
            System.out.println("Sorry file not found");
        }
    }
}