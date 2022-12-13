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

    public static int startTCP() throws IOException {
        ServerSocket socket = new ServerSocket();
        socket.bind(new InetSocketAddress(MY_IP, MY_PORT));
        Socket client = socket.accept();
        DataOutputStream output = new DataOutputStream(client.getOutputStream());
        DataInputStream input = new DataInputStream(client.getInputStream());
        for(int i = 0; i<PACKET_AMOUNT; i++){
            output.write(new byte[BYTE_LENGTH]);
        }
        byte[] ms = new byte[2]; //client "finished" and time
        input.readFully(ms);
        socket.close();
        return fromBytes(ms);
    }

    public static int startUDP(String ip, int port) throws IOException {
        InetAddress address = InetAddress.getByName(ip);
        DatagramSocket socket = new DatagramSocket(new InetSocketAddress(MY_IP, MY_PORT));
        socket.connect(address, port);
        socket.receive(new DatagramPacket(new byte[1], 1)); //client "accept" signal
        int[] runs = new int[1]; //basically atomic
        Thread sender = new Thread(){
            public void run(){
                while(isAlive()){
                    try {
                        Thread.sleep(0, 12); //WHY DOES THIS SLOW TRANSMISSION TIME DOWN BY 10x?! If I don't include it though, I get a packet loss of like 3k+...
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        socket.send(new DatagramPacket(new byte[BYTE_LENGTH], BYTE_LENGTH));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    runs[0]++;
                }
            }
        };
        sender.start();
        byte[] ms = new byte[2]; //client "finished" signal (and time)
        socket.receive(new DatagramPacket(ms, ms.length));
        sender.stop();
        socket.disconnect();
        socket.close();
        System.out.println("Times the sender ran: " + runs[0]); //indicates packet loss
        return fromBytes(ms);
    }

    static short fromBytes(byte[] bytes){
        return (short) (((bytes[0] & 0xFF) << 8) | (bytes[1] & 0xFF));
    }
}
