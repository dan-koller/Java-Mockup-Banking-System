package banking;

import banking.entity.Account;
import banking.entity.Luhn;
import banking.repository.Driver;

import java.util.Objects;
import java.util.Scanner;

public class Application {
    // Variables
    private boolean terminated = false;
    private final Scanner scanner = new Scanner(System.in);

    private Driver driver;

    public Application(String url) {
        driver = new Driver(url);
    }

    // Methods
    public void run() {
        while (!terminated) {
            System.out.println("1. Create an account\n" + "2. Log into account\n" + "0. Exit");
            byte option = scanner.nextByte();
            scanner.nextLine();

            switch (option) {
                case 0:
                    terminated = true;
                    System.exit(0);
                    break;
                case 1:
                    driver.createAccount();
                    break;
                case 2:
                    logIntoAccount();
                    break;
                default:
                    System.out.println("Please select a valid menu option.");
                    break;
            }
        }
    }

    private void logIntoAccount() {
        // Log into existing account
        System.out.println("Enter your card number:");
        String cardNumber = scanner.nextLine();

        System.out.println("Enter your PIN:");
        String pin = scanner.nextLine();

        // Search for matching account
        Account account = driver.findAccount(cardNumber, pin);

        // Verify account
        if (account.getCardNumber().equals(cardNumber) && account.getPin().equals(pin)) {
            boolean isLoggedIn = true;

            System.out.println("\nYou have successfully logged in!\n");

            while (isLoggedIn) {
                // Switch options when logged in
                System.out.println("1. Balance\n" +
                        "2. Add income\n" +
                        "3. Do transfer\n" +
                        "4. Close account\n" +
                        "5. Log out\n" +
                        "0. Exit\n");

                byte loginOption = scanner.nextByte();
                scanner.nextLine();

                switch (loginOption) {
                    case 1:
                        // Print current account balance
                        System.out.println(account.getBalance());
                        break;
                    case 2:
                        // Deposit cash to account
                        System.out.println("Enter income:\n");
                        account.setBalance(scanner.nextLong());
                        scanner.nextLine(); // Catch scanner bug
                        account = driver.updateAccount(account);
                        System.out.println("Income was added!");
                        break;
                    case 3:
                        transfer(account);
                        break;
                    case 4:
                        // Close the account
                        if (driver.deleteAccount(account)) {
                            System.out.println("The account has been closed!");
                        }
                        break;
                    case 5:
                        System.out.println("You have successfully logged out!\n");
                        isLoggedIn = false;
                        break;
                    case 0:
                        System.exit(0);
                        isLoggedIn = false;
                        break;
                }
            }
        } else {
            System.out.println("\nWrong card number or PIN!\n");
        }
    }

    private void transfer(Account account) {
        // Transfer "money" to other account
        System.out.println("Transfer\nEnter card number:");
        String recipentId = scanner.nextLine();

        // Check if entered number is correct
        if (!Luhn.isCorrectNumber(recipentId)) {
            System.out.println("Probably you made mistake in the card number. Please try again!");
            return;
        }

        // Get recipients account
        Account recipient = driver.findAccount(recipentId);

        if (!Objects.equals(recipient.getCardNumber(), recipentId)) {
            System.out.println("Such a card does not exist.");
            return;
        }

        // Handle transfer
        System.out.println("Enter how much money you want to transfer:");
        long money = Long.parseLong(scanner.nextLine());
        if (money > account.getBalance()) {
            System.out.println("Not enough money!");
            return;
        }
        account.setBalance(-money);
        recipient.setBalance(money);
        account = driver.updateAccount(account);
        recipient = driver.updateAccount(recipient);
        System.out.println("Success!");
    }
}
