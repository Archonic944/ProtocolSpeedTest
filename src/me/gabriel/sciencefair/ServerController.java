package me.gabriel.sciencefair;

import java.io.IOException;

public class ServerController {
    static final int TRIALS = 20;
    public static void main(String[] args) throws IOException {
        System.out.println("--------[STARTING TCP]-------");
        int[] tcpTimes = new int[TRIALS];
        for(int i = 0; i<tcpTimes.length; i++){
            tcpTimes[i] = TestServer.startTCP();
            System.out.println("Trial no. " + (i + 1) + ": " + tcpTimes[i] + "ms");
        }
        System.out.println(">>>>>>>[STARTING UDP]>>>>>>>");
        int[] udpTimes = new int[TRIALS];
        for(int i = 0; i<udpTimes.length; i++){
            udpTimes[i] = TestServer.startUDP("localhost", 12344);
            System.out.println("Trial no. " + (i + 1) + ": " + udpTimes[i] + "ms");
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
