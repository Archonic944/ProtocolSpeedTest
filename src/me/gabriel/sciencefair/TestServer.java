package me.gabriel.sciencefair;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class TestServer {
    static final int PACKET_AMOUNT = 10000;
    static final int BYTE_LENGTH = 100;
    static final String MY_IP = "localhost";
    static final int MY_PORT = 12345;

    public static int startTCP(String ip, int port) throws IOException {
        ServerSocket socket = new ServerSocket();
        socket.bind(new InetSocketAddress(MY_IP, MY_PORT));
        Socket client = socket.accept();
        DataOutputStream output = new DataOutputStream(client.getOutputStream());
        DataInputStream input = new DataInputStream(client.getInputStream());
        long startTime = System.currentTimeMillis();
        for(int i = 0; i<PACKET_AMOUNT; i++){
            output.write(new byte[BYTE_LENGTH]);
        }
        input.readFully(new byte[1]);
        socket.close();
        return (int) (System.currentTimeMillis() - startTime);
    }

    public static int startUDP(String ip, int port) throws IOException {
        InetAddress address = InetAddress.getByName(ip);
        DatagramSocket socket = new DatagramSocket();
        socket.bind(new InetSocketAddress(MY_IP, MY_PORT));
        socket.connect(address, port);
        socket.receive(new DatagramPacket(new byte[1], 1)); //client "accept" signal
        long start = System.currentTimeMillis();
        Thread sender = new Thread(){
            public void run(){
                while(isAlive()){
                    try {
                        socket.send(new DatagramPacket(new byte[BYTE_LENGTH], BYTE_LENGTH));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        socket.receive(new DatagramPacket(new byte[2], 2)); //client "finished" signal
        sender.stop();
        socket.disconnect();
        socket.close();
        return (int) (System.currentTimeMillis() - start);
    }
}
