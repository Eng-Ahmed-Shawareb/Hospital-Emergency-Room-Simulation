package com;

public class Semaphore {
    private int capacity;

    public Semaphore(int capacity){
        if(capacity < 0)
            throw new IllegalArgumentException("Capacity cannot be negative");
        this.capacity = capacity;
    }

    public synchronized void acquire() throws InterruptedException{
        while(capacity == 0){
            wait();
        }
        capacity--;
    }

    public synchronized void release(){
        capacity++;
        notifyAll();
    }

    public synchronized boolean tryAcquire(long timeout) throws InterruptedException {
        if (timeout < 0)
            throw new IllegalArgumentException("Timeout cannot be negative");

        long startTime = System.nanoTime();
        long timeoutNanos = timeout * 1_000_000L;
        long remainingTime = timeoutNanos;

        while (capacity == 0) {

            if (remainingTime <= 0)
                return false;

            long millis = remainingTime / 1_000_000;
            int nanos = (int) (remainingTime % 1_000_000);

            wait(millis, nanos);

            long elapsedTime = System.nanoTime() - startTime;
            remainingTime = timeoutNanos - elapsedTime;
        }

        capacity--;
        return true;
    }
}
