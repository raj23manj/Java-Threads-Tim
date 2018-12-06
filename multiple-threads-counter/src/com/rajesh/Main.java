package com.rajesh;

import javafx.beans.binding.When;

public class Main {

    public static void main(String[] args) {
        Countdown countdown = new Countdown();
//        Countdown countdown1 = new Countdown();
//        Countdown countdown2 = new Countdown();
        CountdownThread t1 = new CountdownThread(countdown);
        t1.setName("Thread 1");
        CountdownThread t2 = new CountdownThread(countdown);
        t2.setName("Thread 2");

        t1.start();
        t2.start();

    }
}

//When multiple threads are run with same instance(countdownclass - some work), the instances(countdownclass)
//instance variable will be shared, i,e it will allocated on the heap and shared. if the variables are local
//variables on the instances(countdownclass) it will be added to the thread's Stack

class Countdown {
    private int i;
    // Constructors cannot ne synchronised
    // current thread will hold it until it finishes and then lets it go
    // public synchronized void doCountdown()
    public  void doCountdown() {
        String color;

        switch(Thread.currentThread().getName()) {
            case "Thread 1":
                color = ThreadColor.ANSI_CYAN;
                break;
            case "Thread 2":
                color = ThreadColor.ANSI_PURPLE;
                break;
            default:
                color = ThreadColor.ANSI_GREEN;
        }

        //synchronized(color) wrong, because it is a local variable
        synchronized(this) {
            //for(int i = 10; i > 0; i--)
            for(i = 10; i > 0; i--) {
                System.out.println(color + Thread.currentThread().getName() + " : i = " + i);
            }
        }
    }
}

class CountdownThread extends Thread {
    private Countdown threadCountdown;

    public CountdownThread(Countdown countdown) {
        threadCountdown = countdown;
    }

    public void run() {
        threadCountdown.doCountdown();
    }
}