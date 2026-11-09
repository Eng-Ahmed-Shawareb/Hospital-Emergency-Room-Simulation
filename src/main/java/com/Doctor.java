package com;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Doctor extends Thread{
    private int doctorId;
    private WaitingRoom waitingRoom;
    private TreatmentRoom treatmentRoom;
    private CountDownLatch counter;

    public Doctor(int doctorId, WaitingRoom waitingRoom, TreatmentRoom treatmentRoom, CountDownLatch counter){
        this.doctorId = doctorId;
        this.waitingRoom = waitingRoom;
        this.treatmentRoom = treatmentRoom;
        this.counter = counter;
    }

    @Override
    public void run(){
        while(!Thread.currentThread().isInterrupted()){
            try {
                Patient patient = waitingRoom.fetchPatient();

                Print.printWithTime(
                        "Doctor " + doctorId + " assigned to Patient " + patient.getPatientId()
                );

                Print.printWithTime(
                        "Doctor " + doctorId + " is trying to acquire a treatment room"
                );

                if(treatmentRoom.tryAcquire(3000)){
                    try{
                        Print.printWithTime(
                                "Doctor " + doctorId + " begins treating Patient " + patient.getPatientId()
                        );
                        Random random = new Random();
                        int randomNumber = random.nextInt(5) + 1;
                        sleep(randomNumber * 1000);
                        Print.printWithTime(
                                "Doctor " + doctorId + " finished treating Patient " + patient.getPatientId()
                        );
                    }finally {
                        treatmentRoom.release();

                        Print.printWithTime("Doctor " + doctorId + " released treatment room");
                    }

                    Print.printWithTime("Doctor " + doctorId + " is now available");
                    Print.printWithTime(
                            "Patient " + patient.getPatientId() + " left hospital"
                    );
                    counter.countDown();
                }else{
                    Print.printWithTime(
                            "Doctor " + doctorId + " could not acquire a treatment room"
                    );

                    waitingRoom.returnPatient(patient);

                    Print.printWithTime("Doctor " + doctorId + " is now available");
                }

            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                Print.printWithTime(
                        "Doctor " + doctorId + " was interrupted"
                );
            }
        }
    }

    public int getDoctorId(){
        return doctorId;
    }
}
