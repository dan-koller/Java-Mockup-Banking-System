package banking.entity;

// Implementation of the Luhn algorithm to check card numbers and checksum
public class Luhn {

    public static int getControlNumber(String cardNumber) {
        int sum = 0;

        for (int i = cardNumber.length(); i > 0; i--) {
            int num = Character.getNumericValue(cardNumber.charAt(i - 1));

            if (i % 2 == 1) {
                num *= 2;
            }

            if (num > 9) {
                num -= 9;
            }
            sum += num;
        }
        return sum;
    }

    public static int getCheckSum(String cardNumber) {
        int checksum = 10 - getControlNumber(cardNumber) % 10;
        return checksum % 10;
    }

    public static boolean isCorrectNumber(String cardNumber) {
        // Card number is correct, if it is a multiple of ten
        return getControlNumber(cardNumber) % 10 == 0;
    }

}
