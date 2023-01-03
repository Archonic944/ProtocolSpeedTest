package me.gabriel.sciencefair;

import java.io.IOException;

public class ClientController {
    public static final int TRIALS = 20; //sync this constant with the server code
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("------[RECEIVING TCP]------");
        int[] tcpTimes = new int[TRIALS];
        for(int i = 0; i<TRIALS; i++){
            System.out.println("TCP trial no. " + i + ": " + (tcpTimes[i] = TestClient.connectTCP("192.168.1.146", 12345)) + "ms");
        }
        System.out.println(".:.:.:.[RECEIVING UDP].:.:.:.");
        int[] udpTimes = new int[TRIALS];
        Thread.sleep(50);
        for(int i = 0; i<TRIALS; i++){
            System.out.println("UDP trial no. " + i + ": " + (udpTimes[i] = TestClient.connectUDP("192.168.1.146", 12345)) + "ms");
        }
        System.out.println("-+-+-+-+-+[TEST FINISHED]+-+-+-+-+-");
        System.out.println("TCP average: " + avg(tcpTimes) + "ms");
        System.out.println("Lowest TCP time: " + lowest(tcpTimes) + "ms");
        System.out.println("Highest TCP time: " + highest(tcpTimes) + "ms");

        System.out.println("\nUDP average: " + avg(udpTimes) + "ms");
        System.out.println("Lowest UDP time: " + lowest(udpTimes) + "ms");
        System.out.println("Highest UDP time: " + highest(udpTimes) + "ms");
    }

    static int avg(int[] ints){
        int total = 0;
        for(int anInt : ints){
            total += anInt;
        }
        return total / ints.length;
    }

    static int highest(int[] ints){
        int highest = ints[0];
        for(int num : ints){if(num > highest) highest = num;}
        return highest;
    }
    static int lowest(int[] ints){
        int lowest = ints[0];
        for(int num : ints){if(num < lowest) lowest = num;}
        return lowest;
    }
}
