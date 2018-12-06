package com.rajesh;

import static com.rajesh.ThreadColor.*;

public class Main {

    public static void main(String[] args) {
        System.out.println(ANSI_PURPLE + "Hello World!");
        // implementing via sub-classing Thread class
        final Thread anotherThread = new AnotherThread();
        anotherThread.setName("=== Another Thread ===");

        // invoke run method to start thread
        // start invokes the run method

        //try {
        anotherThread.start(); // prints hello from another thread
         // wrong
        //anotherThread.run(); // prints hello from main

            /*
             *  this throws an exception java.lang.IllegalThreadStateException
             * */
//            anotherThread.start();
//        } catch(IllegalThreadStateException e) {
//            System.out.println(e);
//        }

        // Anonymous Class Thread Creation
        new Thread() {
            public void run() {
                System.out.println(ANSI_GREEN + "Hello From Anonymous Class Thread");
            }
        }.start();

        // implementing the interface
        // normal way
//        Thread myRunnableTHread = new Thread(new MyRunnable());
//        myRunnableTHread.start();
        // anonymous class
        // this preffered way
        Thread myRunnableTHread = new Thread(new MyRunnable() {
            @Override
            public void run() {
                //super.run(); calls MyRunnable classes run
                System.out.println(ANSI_RED + "Hello from MyRunnables implementation Anonymous");
                try{
                    // when a thread is put to sleep here, anotherThread instead making it to sleep
                    // for 3 sec's, we can make it join to this(myRunnableTHread) thread, once this
                    // thread execution is completed it will start anotherThread, then it will execute the code following
                    //  System.out.println(ANSI_RED + "another Thread terminated, so im running again");
                    // o/p will be
                    /*
                    * Hello World!
                        Hello from === Another Thread ===
                        Hello From Anonymous Class Thread
                        Hello World again main thread!
                        Hello from MyRunnables implementation Anonymous
                        3 seconds passed i'm awake
                        another Thread terminated, so im running again
                     * and not
                     * Hello from === Another Thread ===
                        Hello From Anonymous Class Thread
                        Hello World again main thread!
                        Hello from MyRunnables implementation Anonymous
                        another Thread terminated, so im running again
                        3 seconds passed i'm awake
                    * */
                    //anotherThread.join(); // this thread excutes then only goes down to sout
                    anotherThread.join(2000); // see's for 2 sec's, after that just goes on executing
                    /*
                        Hello World!
                        Hello from === Another Thread ===
                        Hello From Anonymous Class Thread
                        Hello World again main thread!
                        Hello from MyRunnables implementation Anonymous
                        another Thread terminated or timed out, so im running again
                        3 seconds passed i'm awake
                    * */
                    System.out.println(ANSI_RED + "another Thread terminated or timed out, so im running again");
                } catch(InterruptedException e) {
                    // catching exception if at all it gets interrupted by some other thread
                    System.out.println(ANSI_RED + "I could'nt wait afterall, i was interuupted !");
                }

            }
        });

        myRunnableTHread.start();
        // interrupting AnotherThread from main thread
        //anotherThread.interrupt();

        System.out.println(ANSI_PURPLE + "Hello World again main thread!");

        /*
        * o/p varies
        * 1) Hello World!
             Hello World again main thread!
             Hello from another thread
          2) Hello World!
             Hello from another thread
             Hello World again main thread!

        * Not allowed to start the instance of thread more than once, if needed create a sub class
        * of Thread Class and run multiple instances
        * */
    }
}
