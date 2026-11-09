package com;

public class Patient extends Thread{
    private int patientId;
    private WaitingRoom waitingRoom;

    public Patient(int patientId, WaitingRoom waitingRoom){
        this.patientId = patientId;
        this.waitingRoom = waitingRoom;
    }

    @Override
    public void run() {
        try {
            Print.printWithTime("Patient " + patientId + " arrived.");
            waitingRoom.enter(this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            Print.printWithTime(
                    "Patient " + patientId + " was interrupted"
            );
        }
    }

    public int getPatientId(){
        return patientId;
    }
}
