package dev.vaibhavsingh;

import dev.vaibhavsingh.client.Authentication;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // Read user input
        Scanner scanner = new Scanner(System.in);

        // Auth
        Authentication.init(scanner);

        // Close scanner
        scanner.close();
    }


}


