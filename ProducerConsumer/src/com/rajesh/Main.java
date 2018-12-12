package com.rajesh;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

import static com.rajesh.Main.EOF;

public class Main {
    public static final String EOF = "EOF";

    public static void main(String[] args) {
//        List<String> buffer = new ArrayList(); // not thread safe
        ArrayBlockingQueue<String> buffer = new ArrayBlockingQueue(6); //Thread safe,  this is bounded and manages locks internally
        //ReentrantLock bufferLock = new ReentrantLock(); // when a thread is holding a lock and enters again it can execute

        // create thread pools, in this case one producer and 2 consumers
        ExecutorService executorService = Executors.newFixedThreadPool(5);

//        MyProducer producer = new MyProducer(buffer, ThreadColor.ANSI_GREEN, bufferLock);
//        MyConsumer consumer1 = new MyConsumer(buffer, ThreadColor.ANSI_PURPLE, bufferLock);
//        MyConsumer consumer2 = new MyConsumer(buffer, ThreadColor.ANSI_CYAN, bufferLock);

        MyProducer producer = new MyProducer(buffer, ThreadColor.ANSI_GREEN);
        MyConsumer consumer1 = new MyConsumer(buffer, ThreadColor.ANSI_PURPLE);
        MyConsumer consumer2 = new MyConsumer(buffer, ThreadColor.ANSI_CYAN);

//        new Thread(producer).start();
//        new Thread(consumer1).start();
//        new Thread(consumer2).start();
        executorService.execute(producer);
        executorService.execute(consumer1);
        executorService.execute(consumer2);

        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println(ThreadColor.ANSI_RED + "I'M BEING PRINTED FOR THE CALLABLE CLASS");
                return "This is the callable result";
            }
        });

        try {
            // the main thread hold's until this is resolved
            System.out.println(future.get());
        } catch (ExecutionException e) {
            System.out.println("Something went wrong");
        } catch (InterruptedException e) {
            System.out.println("Thread running the task was interrupted");
        }

        // will wait untill all scheduled class finishes, shut downs in order
        executorService.shutdown();
    }
}


class MyProducer implements Runnable {
//    private List<String> buffer;
//    private String color;
//    private ReentrantLock bufferLock;
//
//    MyProducer(List<String> buffer, String color, ReentrantLock bufferLock) {
//        this.buffer = buffer;
//        this.color = color;
//        this.bufferLock = bufferLock;
//    }

    private ArrayBlockingQueue<String> buffer;
    private String color;
    //private ReentrantLock bufferLock;

    MyProducer(ArrayBlockingQueue<String> buffer, String color) {
        this.color = color;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        Random random = new Random();
        String[] nums = {"1", "2", "3", "4", "5"};

        for(String num: nums) {
            try {
                System.out.println(color + "Adding... " + num);
                buffer.put(num);
                //bufferLock.lock();
//                try{
//                    buffer.add(num);
//                } finally {
//                    bufferLock.unlock();
//                }

//                2
//                bufferLock.lock();
//                buffer.add(num);
//                bufferLock.unlock();
//
               //1
               // synchronized (buffer) {
               //     buffer.add(num);
               // }

                Thread.sleep(random.nextInt(2000));
            } catch(InterruptedException e) {
                System.out.println("Producer was Interrupted");
            }
        }

        System.out.println(color + "Adding EOF and Exiting...");
        //bufferLock.lock();
        try{
            buffer.put("EOF");
          //buffer.add("EOF");
        } catch (InterruptedException e) {

        }

//        finally {
//          bufferLock.unlock();
//        }

       // synchronized (buffer) {
       //     buffer.add("EOF");
       // }

    }
}


class MyConsumer implements Runnable {
//    private List<String> buffer;
    private ArrayBlockingQueue<String> buffer;
    private String color;
   // ReentrantLock bufferLock;

//    MyConsumer(List<String> buffer, String color, ReentrantLock bufferLock) {
//        this.buffer = buffer;
//        this.color = color;
//        this.bufferLock = bufferLock;
//    }

    MyConsumer(ArrayBlockingQueue<String> buffer, String color) {
        this.buffer = buffer;
        this.color = color;
    }

    @Override
    public void run() {
        //int counter = 0;
        while(true) {
            //synchronized (buffer) {
//            bufferLock.lock();
//                if(buffer.isEmpty()){
//                    bufferLock.unlock();
//                    continue;
//                }
//
//                if(buffer.get(0).equals(EOF)) {
//                    System.out.println(color + "Exiting");
//                    bufferLock.unlock();
//                    break;
//                } else {
//                    System.out.println(color + "Removed" + buffer.remove(0));
//                }
//            bufferLock.unlock();
           // }


//            bufferLock.lock();
//            try {
//                if(buffer.isEmpty()){
//                    continue;
//                }
//
//                if(buffer.get(0).equals(EOF)) {
//                    System.out.println(color + "Exiting");
//                    break;
//                } else {
//                    System.out.println(color + "Removed" + buffer.remove(0));
//                }
//            } finally {
//                bufferLock.unlock();
//            }

            // one more version trylock()

//            if(bufferLock.tryLock()) {
//                try {
//                    if(buffer.isEmpty()){
//                        continue;
//                    }
//                    System.out.println(color + "The Counter = " + counter);
//                    counter = 0;
//                    if(buffer.get(0).equals(EOF)) {
//                        System.out.println(color + "Exiting");
//                        break;
//                    } else {
//                        System.out.println(color + "Removed" + buffer.remove(0));
//                    }
//                } finally {
//                    bufferLock.unlock();
//                }
//            } else {
//                counter++;
//            }
                synchronized (buffer) {
                    try {
                        if(buffer.isEmpty()){
                            continue;
                        }

                        if(buffer.peek().equals(EOF)) {
                            System.out.println(color + "Exiting");
                            break;
                        } else {
                            System.out.println(color + "Removed" + buffer.take());
                        }
                    } catch (InterruptedException e) {

                    }
                }


        }
    }
}



