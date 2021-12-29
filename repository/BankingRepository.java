package banking.repository;

import banking.entity.Account;

public interface BankingRepository {
    void createAccount();

    Account findAccount(String cardNumber, String pin);

    Account findAccount(String cardNumber);

    Account updateAccount(Account account);

    boolean deleteAccount(Account account);
}
