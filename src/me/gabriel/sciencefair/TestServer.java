package me.gabriel.sciencefair;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class TestServer {
    static final int BYTE_LENGTH = 1000;
    static final int PACKET_AMOUNT = 10000;
    static final String MY_IP = "192.168.1.146";
    static final int MY_PORT = 12345;

    public static int startTCP(Socket client) throws IOException {
        DataOutputStream output = new DataOutputStream(client.getOutputStream());
        DataInputStream input = new DataInputStream(client.getInputStream());
        for(int i = 0; i<PACKET_AMOUNT; i++){
            output.write(new byte[BYTE_LENGTH]);
        }
        byte[] ms = new byte[2]; //client "finished" and time
        input.readFully(ms);
        client.close();
        return fromBytes(ms);
    }

    /**
     * @deprecated Now using {@code beginUDPFirehose} instead.
     */
    @Deprecated
    public static int startUDP(String ip, int port) throws IOException, InterruptedException {
        InetAddress address = InetAddress.getByName(ip);
        DatagramSocket socket = new DatagramSocket(new InetSocketAddress(MY_IP, MY_PORT));
        socket.connect(address, port);
        socket.receive(new DatagramPacket(new byte[1], 1)); //client "accept" signal
        int[] runs = new int[1]; //basically atomic
        Thread sender = new Thread(){ //sends packets in a forever loop, and only stops when the other thread receives the "end" signal (just means the client has received 10000)
            public void run(){
                while(isAlive()){
//                    if(runs[0] % 400 == 0){
//                        try {
//                            Thread.sleep(1);
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
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

    /**
     * This method runs until manually stopped.
     */
    public static void beginUDPFirehose(String ip, int port) throws IOException {
        DatagramSocket socket = new DatagramSocket(MY_PORT, InetAddress.getByName(MY_IP));
        while(true){
            try {
                socket.send(new DatagramPacket(new byte[BYTE_LENGTH], BYTE_LENGTH, InetAddress.getByName(ip), port));
            }catch(Exception ignored){
            }
        }
    }

    static short fromBytes(byte[] bytes){
        return (short) (((bytes[0] & 0xFF) << 8) | (bytes[1] & 0xFF));
    }
}
