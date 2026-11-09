package com;

public class TreatmentRoom {
    private Semaphore rooms;

    public TreatmentRoom(){
        this.rooms = new Semaphore(2);
    }

    public boolean tryAcquire(long timeout) throws InterruptedException{
        return rooms.tryAcquire(timeout);
    }

    public void release(){
        rooms.release();
    }
}
