package banking.repository;

import banking.entity.Account;
import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class Driver implements BankingRepository {
    // SQL statements
    private static final String SQL_ADD_ACCOUNT = "INSERT INTO card (number, pin) VALUES (?, ?)";
    private static final String SQL_LOGIN_ACCOUNT = "SELECT number, pin, balance FROM card WHERE number = ? AND pin = ?";
    private static final String SQL_FIND_ACCOUNT = "SELECT number, pin, balance FROM card WHERE number = ?";
    private static final String SQL_UPDATE_ACCOUNT = "UPDATE card SET balance = ? WHERE number = ?";
    private static final String SQL_DELETE_ACCOUNT = "DELETE FROM card WHERE number = ?";

    private String url;

    // Constructor
    public Driver(String url) {
        this.url = url;

        // Create new database if it doesn't exist
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(this.url);

        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {
                // Statement execution
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                        "id INTEGER," +
                        "number TEXT NOT NULL," +
                        "pin TEXT NOT NULL," +
                        "balance INTEGER DEFAULT 0)");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Methods
    @Override
    public void createAccount() {
        // Create a new account
        Account account = new Account();
        account.generateDetails();
        account.setBalance(0);

        System.out.println("Your card has been created");
        System.out.println("Your card number:\n" + account.getCardNumber());
        System.out.println("Your card PIN:\n" + account.getPin() + "\n");

        // Store account in database
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement sql = connection.prepareStatement(SQL_ADD_ACCOUNT)) {

            sql.setString(1, account.getCardNumber());
            sql.setString(2, account.getPin());
            sql.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Account findAccount(String cardNumber, String pin) {
        try (final var connection = DriverManager.getConnection(url);
             final var sql = connection.prepareStatement(SQL_FIND_ACCOUNT)) {

            sql.setString(1, cardNumber);
            ResultSet resultSet = sql.executeQuery();

            return new Account(resultSet.getString("number"), resultSet.getString("pin"), resultSet.getLong("balance"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Account();
    }

    @Override
    public Account findAccount(String cardNumber) {
        try (final var connection = DriverManager.getConnection(url);
             final var sql = connection.prepareStatement(SQL_FIND_ACCOUNT)) {

            sql.setString(1, cardNumber);
            ResultSet resultSet = sql.executeQuery();

            return new Account(resultSet.getString("number"), resultSet.getString("pin"), resultSet.getLong("balance"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Account();
    }

    @Override
    public Account updateAccount(Account account) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement sql = connection.prepareStatement(SQL_UPDATE_ACCOUNT)) {

            sql.setLong(1, account.getBalance());
            sql.setString(2, account.getCardNumber());
            sql.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return findAccount(account.getCardNumber());
    }

    @Override
    public boolean deleteAccount(Account account) {
        try (final var connection = DriverManager.getConnection(url);
             final var sql = connection.prepareStatement(SQL_DELETE_ACCOUNT)) {

            sql.setString(1, account.getCardNumber());
            sql.executeUpdate();
            sql.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
