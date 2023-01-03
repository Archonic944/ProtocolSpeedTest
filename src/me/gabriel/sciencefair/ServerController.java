package me.gabriel.sciencefair;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class ServerController {
    static final int TRIALS = 5;
    public static void main(String[] args) throws IOException {
        System.out.println("   Beginning TCP trials...");
        int[] tcpTimes = new int[TRIALS];
        ServerSocket socket = new ServerSocket();
        socket.bind(new InetSocketAddress(TestServer.MY_IP, TestServer.MY_PORT));
        for(int i = 0; i<tcpTimes.length; i++){
            tcpTimes[i] = TestServer.startTCP(socket.accept());
            System.out.println("Trial no. " + i + ": " + tcpTimes[i] + "ms");
        }
        socket.close();
        System.out.println("   Starting UDP firehose...");
        TestServer.beginUDPFirehose("192.168.1.70", 12344);
    }
}
