package banking;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:./" + args[1];

        // Start app
        new Application(url).run();

    }
}
