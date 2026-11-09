package com;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Hospital {
    static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException{
        startSimulation();
    }

    public static void startSimulation() throws InterruptedException{
        System.out.println("===========================================");
        System.out.println("               Hospital Emergency Room                ");
        System.out.println("===========================================");

        int waitingRoomCapacity;
        int numberOfPatients;
        int numberOfDoctors;

        System.out.println("Enter waiting room capacity : ");
        waitingRoomCapacity = input.nextInt();
        System.out.println("Enter number of patients : ");
        numberOfPatients = input.nextInt();
        System.out.println("Enter number of doctors : ");
        numberOfDoctors = input.nextInt();

        WaitingRoom waitingRoom = new WaitingRoom(waitingRoomCapacity);
        TreatmentRoom treatmentRoom = new TreatmentRoom();

        CountDownLatch counter = new CountDownLatch(numberOfPatients);

        ArrayList<Patient> patients = new ArrayList<>();
        ArrayList<Doctor> doctors = new ArrayList<>();

        for(int i = 0 ; i < numberOfPatients ; ++i){
            Patient patient = new Patient(i + 1 , waitingRoom);
            patients.add(patient);
        }

        for(int i = 0 ; i < numberOfDoctors ; ++i){
            Doctor doctor = new Doctor(i + 1 , waitingRoom , treatmentRoom, counter);
            doctors.add(doctor);
        }

        System.out.println();
        System.out.println("===========================================");
        System.out.println("                         Start Simulation                     ");
        System.out.println("===========================================");

        for (Doctor doctor : doctors)
            doctor.start();

        for (Patient patient : patients)
            patient.start();

        for (Patient patient : patients)
            patient.join();

        counter.await();

        Print.printWithTime("All patients have been treated.");

        for (Doctor doctor : doctors)
            doctor.interrupt();

        for (Doctor doctor : doctors)
            doctor.join();

        Print.printWithTime("Simulation completed.");
    }

}
