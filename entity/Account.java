package banking.entity;

import java.util.Random;

public class Account {

    // Variables
    private String cardNumber = "";
    private String pin = "";
    private long balance = 0;

    private final String BIN = "400000";

    // Random number generator
    private final Random random = new Random();

    // Constructors
    public Account() {}

    public Account(String cardNumber, String pin, long balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    // Methods
    public void generateDetails() {
        generateNumber();
        generatePin();
    }

    public void generateNumber() {
        // Add banking identifier to card number
        cardNumber += BIN;

        // Generate random account numbers
        for (int i = 0; i < 9; i++) {
            int num = random.nextInt(9) + 1;
            cardNumber += Integer.toString(num);
        }

        // Generate Checksum
        cardNumber += Integer.toString(Luhn.getCheckSum(cardNumber));

//        // Check if it is correct
//        System.out.println(Luhn.isCorrectNumber(cardNumber));
    }

    public void generatePin() {
        for (int i = 0; i < 4; i++) {
            int num = random.nextInt(9) + 1;
            pin += Integer.toString(num);
        }
    }

    // Getters
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setBalance(long balance) {
        this.balance += balance;
    }

    public long getBalance() {
        return balance;
    }
}
