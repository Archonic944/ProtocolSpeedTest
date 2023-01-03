package me.gabriel.sciencefair;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class TestClient {
    public static final int BYTE_LENGTH = 100;
    public static final int PACKET_AMOUNT = 10000;
    public static final int MY_PORT = 12344;
    public static final String MY_IP = "192.168.1.70";

    public static int connectUDP(String ip, int port) throws IOException, InterruptedException {
        DatagramSocket socket = new DatagramSocket(MY_PORT, InetAddress.getByName(MY_IP));
        long start = System.nanoTime();
        for(int i = 0; i<PACKET_AMOUNT; i++){
            socket.receive(new DatagramPacket(new byte[BYTE_LENGTH], BYTE_LENGTH));
            if(i % 50 == 0) System.out.println("Received packet no. " + i);
        }
        short ms = (short) ((System.nanoTime() - start) / 1000000); //aaand time!
        socket.close();
        return ms;
    }

    public static int connectTCP(String ip, int port) throws IOException, InterruptedException {
        Thread.sleep(1);
        Socket client = new Socket();
        client.connect(new InetSocketAddress(ip, port));
        DataInputStream input = new DataInputStream(client.getInputStream());
        DataOutputStream output = new DataOutputStream(client.getOutputStream());
        long start = System.nanoTime();
        for(int i = 0; i<PACKET_AMOUNT; i++){
            input.readFully(new byte[BYTE_LENGTH]);
        }
        short ms = (short) (((System.nanoTime() - start) / 1000000));
        output.write(toBytes(ms));
        client.close();
        return ms;
    }

    static byte[] toBytes(short sh){
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (sh >> 8);
        bytes[1] = (byte) sh;
        return bytes;
    }
}
