package com;

import java.util.LinkedList;
import java.util.Queue;

public class WaitingRoom {
    private int capacity;
    private Queue<Patient> patients;
    private Semaphore empty;
    private Semaphore full;
    private Semaphore mutex;

    public WaitingRoom(int capacity){
        if (capacity <= 0){
            throw new IllegalArgumentException("Capacity must be positive");
        }

        this.capacity = capacity;
        patients = new LinkedList<>();
        empty = new Semaphore(this.capacity);
        full = new Semaphore(0);
        mutex = new Semaphore(1);
    }

    public void enter(Patient patient) throws InterruptedException{
        if (!empty.tryAcquire(0)) {
            Print.printWithTime(
                    "Patient " + patient.getPatientId() +
                            " is waiting outside because the waiting room is full"
            );
            empty.acquire();
        }

        try {
            mutex.acquire();
        }catch (InterruptedException e){
            empty.release();
            throw e;
        }

        try {
            patients.add(patient);
            Print.printWithTime("Patient " + patient.getPatientId() + "entered waiting room");
        }finally {
            mutex.release();
        }

        full.release();
    }

    public Patient fetchPatient() throws InterruptedException{
        Patient patient;
        full.acquire();

        try {
            mutex.acquire();
        }catch (InterruptedException e){
            full.release();
            throw e;
        }

        try {
            patient = patients.poll();
        }finally {
            mutex.release();
        }

        empty.release();

        return patient;
    }

    public void returnPatient(Patient patient) throws InterruptedException{
        Print.printWithTime(
                "Patient " + patient.getPatientId() + "sending back to the waiting room"
        );
        enter(patient);
    }
}
