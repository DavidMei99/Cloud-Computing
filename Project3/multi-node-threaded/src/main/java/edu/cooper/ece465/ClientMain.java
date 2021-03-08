package edu.cooper.ece465;

public class ClientMain {
    public static void main(String[] args) {

        Client client = new Client("localhost", 6666);
        long runtime = client.run();
        System.out.println(runtime);
    }
}
