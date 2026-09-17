/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.bonolo.poe.prog5121_p1;

/**
 *
 * @author bonol
 */
import java.util.Scanner;
public class PROG5121_P1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- REGISTRATION ---");
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        
        System.out.print("Enter Username (must contain _ and <=5 chars): ");
        String username = scanner.nextLine();
        
        System.out.print("Enter Password (8+ chars, capital, number, special): ");
        String password = scanner.nextLine();
        
        System.out.print("Enter SA Cell Number (e.g. +27838968976): ");
        String cellNumber = scanner.nextLine();

        Login login = new Login(firstName, lastName, username, password, cellNumber);
        String registrationMessage = login.registerUser();
        System.out.println(registrationMessage);

        
        if (registrationMessage.contains("successfully")) {
            System.out.println("\n--- LOGIN ---");
            System.out.print("Enter Username: ");
            String loginUsername = scanner.nextLine();
            System.out.print("Enter Password: ");
            String loginPassword = scanner.nextLine();

            boolean isLoggedIn = login.loginUser(loginUsername, loginPassword);
            System.out.println(login.returnLoginStatus(isLoggedIn));
        }
        scanner.close();
    }
}
