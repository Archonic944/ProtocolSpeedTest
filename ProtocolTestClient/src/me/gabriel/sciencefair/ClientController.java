package me.gabriel.sciencefair;

import java.io.IOException;

public class ClientController {
    public static final int TRIALS = 20; //sync this constant with the server code
    public static void main(String[] args) throws IOException, InterruptedException {
        for(int i = 0; i<TRIALS; i++){
            TestClient.connectTCP("localhost", 12345);
        }
        Thread.sleep(50);
        for(int i = 0; i<TRIALS; i++){
            TestClient.connectUDP("localhost", 12345);
        }
    }
}
