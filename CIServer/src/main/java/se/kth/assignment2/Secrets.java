package se.kth.assignment2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Secrets {
    protected String ACCESS_TOKEN = "";

    public Secrets() {
        try {
            File file = new File("token.txt");

            Scanner scanner = new Scanner(file);

            ACCESS_TOKEN = scanner.nextLine();
            scanner.close();
            
        } catch (FileNotFoundException e) {
            // TODO: handle exception
            System.out.println("INSIDE CATCH!");
            ACCESS_TOKEN = "abcd";
        }
    }
    
}
