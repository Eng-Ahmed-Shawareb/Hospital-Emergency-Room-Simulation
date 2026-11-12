# Hospital Emergency Room Simulation

An **Operating Systems course project** written in Java that simulates a hospital emergency room using multithreading and synchronization. The project demonstrates the **Producer–Consumer problem** with a **Bounded Buffer**, using custom semaphores to coordinate patient arrivals, doctors, and the limited treatment rooms.

## Project Overview

Patients arrive concurrently and try to enter a waiting room with a user-defined capacity. Doctors run concurrently, wait for available patients, and attempt to acquire one of the hospital's two treatment rooms. If a doctor cannot acquire a room within three seconds, the patient is returned to the waiting room. Otherwise, the doctor treats the patient for a random duration.

## Operating Systems Concepts

- **Multithreading:** Patients and doctors execute as separate Java threads.
- **Producer–Consumer problem:** Patients produce work by joining the waiting queue, while doctors consume patients from it.
- **Bounded Buffer:** The waiting room is represented by a queue with a fixed capacity.
- **Semaphores:** A custom semaphore implementation coordinates access to shared resources.
- **Mutual exclusion:** A mutex semaphore protects concurrent modifications to the waiting-room queue.
- **Timed resource acquisition:** Doctors have up to three seconds to acquire a treatment room.
- **Thread coordination:** A `CountDownLatch` tracks completed treatments, while interruption and `join()` are used to coordinate doctor shutdown.

## Main Components

| Class | Responsibility |
|---|---|
| `Hospital` | Reads simulation settings, creates shared resources and threads, starts the simulation, and coordinates its completion. |
| `Patient` | Represents a patient thread that arrives and attempts to enter the waiting room. |
| `Doctor` | Represents a doctor thread that retrieves patients, acquires a treatment room, performs treatment, or returns a patient to the queue. |
| `WaitingRoom` | Implements the bounded waiting queue and coordinates producers and consumers. |
| `TreatmentRoom` | Controls access to the two available treatment rooms. |
| `Semaphore` | Implements counting-semaphore operations: `acquire()`, `release()`, and timed `tryAcquire()`. |
| `Print` | Prints simulation events with timestamps. |

## Synchronization Design

### Waiting Room

The waiting room uses a queue and three semaphores:

| Semaphore | Initial value | Purpose |
|---|---:|---|
| `empty` | Waiting-room capacity | Counts available seats. |
| `full` | `0` | Counts patients available for doctors to fetch. |
| `mutex` | `1` | Ensures exclusive access to the shared queue. |

### Treatment Rooms

The hospital has **two treatment rooms**. A counting semaphore initialized with two permits ensures that no more than two doctors can hold treatment-room permits at the same time.

## Simulation Workflow

1. The program asks for the waiting-room capacity, number of patients, and number of doctors.
2. Doctor threads start and wait for patients.
3. Patient threads arrive and attempt to enter the bounded waiting queue.
4. A doctor fetches a patient and tries to acquire a treatment room within **three seconds**.
5. If acquisition fails, the doctor returns the patient to the waiting queue.
6. If acquisition succeeds, the doctor treats the patient for a random duration of **one to five seconds**, then releases the treatment-room permit.
7. After all patients are treated, the hospital coordinates the termination of the doctor threads.

## Requirements

- Java Development Kit (JDK) 8 or later.
- No external libraries are required; the project uses the Java standard library.

## How to Run

Run these commands from the project root, where the `com` directory is located:

```bash
javac com/*.java
java com.Hospital
```

The program will prompt for:

```text
Enter waiting room capacity:
Enter number of patients:
Enter number of doctors:
```

Use a positive waiting-room capacity and at least one doctor when the number of patients is greater than zero.

## Project Context

This project was developed as part of an **Operating Systems** course assignment to practice thread creation, synchronization, semaphores, and the Producer–Consumer pattern in Java.