package me.gabriel.sciencefair;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class TestClient {
    public static final int BYTE_LENGTH = 100;
    public static final int PACKET_AMOUNT = 10000;
    public static final int MY_PORT = 12344;
    public static final String MY_IP = "localhost";

    public static void connectUDP(String ip, int port) throws IOException {
        InetSocketAddress targetAddr = new InetSocketAddress(ip, port);
        DatagramSocket socket = new DatagramSocket(MY_PORT, InetAddress.getByName(MY_IP));
        socket.connect(targetAddr);
        socket.send(new DatagramPacket(new byte[1], 1, targetAddr)); //ready to start
        for(int i = 0; i<PACKET_AMOUNT; i++){
            socket.receive(new DatagramPacket(new byte[BYTE_LENGTH], BYTE_LENGTH, targetAddr));
        }
        socket.send(new DatagramPacket(new byte[2], 2, targetAddr)); //client "finished" signal
        socket.disconnect();
        socket.close();
    }

    public static void connectTCP(String ip, int port) throws IOException, InterruptedException {
        Thread.sleep(100);
        Socket client = new Socket();
        client.bind(new InetSocketAddress(MY_IP, MY_PORT));
        client.connect(new InetSocketAddress(ip, port));
        DataInputStream input = new DataInputStream(client.getInputStream());
        DataOutputStream output = new DataOutputStream(client.getOutputStream());
        for(int i = 0; i<PACKET_AMOUNT; i++){
            input.readFully(new byte[BYTE_LENGTH]);
        }
        output.write(new byte[1]);
        client.shutdownOutput();
        client.close();
    }
}
