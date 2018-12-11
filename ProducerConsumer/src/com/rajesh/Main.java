package com.rajesh;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.rajesh.Main.EOF;

public class Main {
    public static final String EOF = "EOF";

    public static void main(String[] args) {
        List<String> buffer = new ArrayList(); // not thread safe
        MyProducer producer = new MyProducer(buffer, ThreadColor.ANSI_GREEN);
        MyConsumer consumer1 = new MyConsumer(buffer, ThreadColor.ANSI_PURPLE);
        MyConsumer consumer2 = new MyConsumer(buffer, ThreadColor.ANSI_CYAN);

        new Thread(producer).start();
        new Thread(consumer1).start();
        new Thread(consumer2).start();
    }
}


class MyProducer implements Runnable {
    private List<String> buffer;
    private String color;

    MyProducer(List<String> buffer, String color) {
        this.buffer = buffer;
        this.color = color;
    }

    @Override
    public void run() {
        Random random = new Random();
        String[] nums = {"1", "2", "3", "4", "5"};

        for(String num: nums) {
            try {
                System.out.println(color + "Adding... " + num);
                buffer.add(num);

                Thread.sleep(random.nextInt(2000));
            } catch(InterruptedException e) {
                System.out.println("Producer was Interrupted");
            }
        }

        System.out.println(color + "Adding EOF and Exiting...");
        buffer.add("EOF");
    }
}


class MyConsumer implements Runnable {
    private List<String> buffer;
    private String color;

    MyConsumer(List<String> buffer, String color) {
        this.buffer = buffer;
        this.color = color;
    }

    @Override
    public void run() {
        while(true) {
            if(buffer.isEmpty()){
                continue;
            }

            if(buffer.get(0).equals(EOF)) {
                System.out.println(color + "Exiting");
            } else {
                System.out.println(color + "Removed" + buffer.remove(0));
            }
        }
    }
}



