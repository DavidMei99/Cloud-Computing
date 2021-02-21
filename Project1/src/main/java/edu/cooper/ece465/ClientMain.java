package edu.cooper.ece465;

public class ClientMain {
    public static void main(String[] args) {

        Client client = new Client("localhost", 6668);
        long runtime = client.run();

    }
}
