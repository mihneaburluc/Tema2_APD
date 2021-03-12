package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.*;
import com.apd.tema2.utils.Pair;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

/**
 * Clasa Factory ce returneaza implementari ale InterfaceHandler sub forma unor
 * clase anonime.
 */
public class IntersectionHandlerFactory {

    public static IntersectionHandler getHandler(String handlerType) {

        return switch (handlerType) {
            case "simple_semaphore" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    System.out.println("Car " + car.getId() + " has reached the semaphore, " +
                            "now waiting...");
                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Car " + car.getId() + " has waited enough, now driving...");
                }
            };
            case "simple_n_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    Intersection intersection =
                            IntersectionFactory.getIntersection(handlerType);
                    // in taskurile unde exista un singur semafor, parametrul lui get va fi 0 sau -1
                    Semaphore sem = intersection.getSemaphore(-1);
                    System.out.println("Car " + car.getId() + " has reached the roundabout, " +
                            "now waiting...");
                    try {
                        sem.acquire();
                        System.out.println("Car " + car.getId() + " has entered the roundabout");
                        sleep(intersection.getT());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " has exited the roundabout after "
                            + (intersection.getT() / 1000) + " seconds");
                    sem.release();
                }
            };
            case "simple_strict_1_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    Intersection intersection =
                            IntersectionFactory.getIntersection(handlerType);

                    Semaphore sem = intersection.getSemaphore(car.getStartDirection());
                    CyclicBarrier barrier = intersection.getBarrier();
                    System.out.println("Car " + car.getId() + " has reached the roundabout");
                    try {
                        sem.acquire();
                        barrier.await();
                        System.out.println("Car " + car.getId() + " has entered the roundabout from lane "
                                + car.getStartDirection());
                        sleep(intersection.getT());
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " has exited the roundabout after "
                            + (intersection.getT() / 1000) + " seconds");
                    sem.release();

                }
            };
            case "simple_strict_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    Intersection intersection =
                            IntersectionFactory.getIntersection(handlerType);

                    Semaphore sem = intersection.getSemaphore(car.getStartDirection());
                    CyclicBarrier barrier = intersection.getBarrier();
                    System.out.println("Car " + car.getId() + " has reached the roundabout, " +
                            "now waiting...");

                    try {
                        // bariera din main este bariera care asteapta toata masinile
                        // taskul specifica ca trebuie sa soseasca toate masinile in sens
                        // si dupa se face selectia lor pentru a intra in sens
                        Main.barrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    try {
                        sem.acquire();
                        barrier.await();
                        System.out.println("Car " + car.getId() + " was selected to enter the " +
                                "roundabout from lane " + car.getStartDirection());
                        barrier.await();
                        System.out.println("Car " + car.getId() + " has entered the roundabout from lane "
                                + car.getStartDirection());
                        sleep(intersection.getT());
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " has exited the roundabout after "
                            + (intersection.getT() / 1000) + " seconds");
                    sem.release();
                }
            };
            case "simple_max_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    Intersection intersection =
                            IntersectionFactory.getIntersection(handlerType);

                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI

                    Semaphore sem = intersection.getSemaphore(car.getStartDirection());
                    System.out.println("Car " + car.getId() + " has reached the roundabout from lane "
                            + car.getStartDirection());

                    try {
                        sem.acquire();
                        System.out.println("Car " + car.getId() + " has entered the roundabout from lane "
                                + car.getStartDirection());
                        sleep(intersection.getT());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " has exited the roundabout after "
                            + (intersection.getT() / 1000) + " seconds");
                    sem.release();
                }
            };
            case "priority_intersection" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    Intersection intersection =
                            IntersectionFactory.getIntersection(handlerType);
                    Semaphore sem = intersection.getSemaphore(0);

                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI

                    if (car.getPriority() == 1) {

                        // se printeaza pe rand mesajele ca masinile fara prioritate
                        // au ajuns la intersectie
                        synchronized (this) {
                            System.out.println("Car " + car.getId() + " with low priority " +
                                    "is trying to enter the intersection...");

                            // daca nu sunt masini cu prioritate in intersectie
                            try {
                                sem.acquire();
                                System.out.println("Car " + car.getId() + " with low priority " +
                                        "has entered the intersection");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            sem.release();
                        }

                    } else {
                        // tinem minte cate masini sunt in intersectie cu inIntersection
                        // daca nu e nico masina oprim semaforul pentru ca nicio masina
                        // fara prioritate sa nu poata intra pana nu se goleste intersectia
                        if (Main.inIntersection.get() == 0) {
                            try {
                                sem.acquire();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        Main.inIntersection.addAndGet(1);
                        System.out.println("Car " + car.getId() + " with high priority " +
                                "has entered the intersection");

                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        System.out.println("Car " + car.getId() + " with high priority " +
                                "has exited the intersection");
                        Main.inIntersection.addAndGet(-1);

                        // daca nu mai sunt masini in intersectie, pot intra cele fara prioritate
                        if (Main.inIntersection.get() == 0) {
                            sem.release();
                        }
                    }
                }
            };
            case "crosswalk" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    boolean msgRed = false;
                    boolean msgGreen = false;

                    while(!Main.pedestrians.isFinished()) {
                        if (Main.pedestrians.isPass() && !msgRed) {
                            msgRed = true;
                            System.out.println("Car " + car.getId() + " has now red light");
                            msgGreen = false;
                        }

                        if (!Main.pedestrians.isPass() && !msgGreen) {
                            msgGreen = true;
                            System.out.println("Car " + car.getId() + " has now green light");
                            msgRed = false;
                        }
                    }

                    if (!msgGreen) {
                        System.out.println("Car " + car.getId() + " has now green light");
                    }
                }
            };
            case "simple_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    Intersection intersection =
                            IntersectionFactory.getIntersection(handlerType);

                    // 2 semafoare pt fiecare sens
                    // unul va permite x masini, cel din sens opus va permtie 0
                    Semaphore sem0 = intersection.getSemaphore(0);
                    Semaphore sem1 = intersection.getSemaphore(1);
                    CyclicBarrier barrier = intersection.getBarrier();

                    System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection()
                            + " has reached the bottleneck");

                    if (car.getStartDirection() == 0) {
                        try {
                            sem0.acquire();
                            System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection()
                                    + " has passed the bottleneck");
                            barrier.await();
                        } catch (InterruptedException | BrokenBarrierException e) {
                            e.printStackTrace();
                        }
                        sem1.release();
                    } else {
                        try {
                            sem1.acquire();
                            System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection()
                                    + " has passed the bottleneck");
                            barrier.await();
                        } catch (InterruptedException | BrokenBarrierException e) {
                            e.printStackTrace();
                        }
                        sem0.release();
                    }
                }
            };
            case "complex_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    Intersection intersection =
                            IntersectionFactory.getIntersection(handlerType);
                    System.out.println("Car " + car.getId() +
                            " has come from the lane number " + car.getStartDirection());
                    synchronized ((Object) car.getStartDirection()) {
                        ArrayBlockingQueue<Integer> queueInitialLane =
                                intersection.getQueue(car.getStartDirection());
                        queueInitialLane.add(car.getId());
                    }

                    // toate masinile ajung la intrarea drumului in lucru
                    // si sunt adaugate in coada cu banda de pe care fac parte
                    try {
                        Main.barrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    if (car.getId() == 0) {

                        // numarul initial de benzi
                        int n = intersection.getN();
                        // numarul de benzi noi
                        int m = intersection.getT();
                        // numarul de maxim de masini care voie sa treaca odata
                        int x = intersection.getX();
                        // i este lane-ul pe care ajunge masina, iar number este cel initial


                        for (int i = 0; i < m; i++) {
                            ArrayBlockingQueue<Pair> queue =
                                    new ArrayBlockingQueue<>(n);
                            // repartizam fiecare lane vechi pe un lane nou
                            // si adaugam in coada lane-ului nou toate benzile vechi asignate lui
                            // benzile vechi sunt reprezentate de o coada cu id-urile tuturor masinilor
                            // care circulau initial pe ele
                            int start = (int) (i * (double) n / m);
                            int end = (int) Math.min((i + 1) * (double) n / m, n);

                            for (int j = start; j < end; j++) {
                                queue.add(new Pair(intersection.getQueue(j), j));
                            }

                            // cat timp mai sunt masini pe benzile vechi
                            while (!queue.isEmpty()) {
                                int count = x;
                                Pair p = queue.poll();
                                ArrayBlockingQueue<Integer> currentQueue = p.queue;
                                int number = p.n;

                                // cat timp mai sunt masini de trecut de pe banda number
                                while (count != 0) {
                                    if (!currentQueue.isEmpty()) {
                                        System.out.println("Car " + currentQueue.poll() + " from the lane "
                                                + number + " has entered lane number " + i);
                                    }

                                    if (currentQueue.isEmpty()) {
                                        System.out.println("The initial lane " + number +
                                                " has been emptied and removed from the new lane queue");
                                        break;
                                    }

                                    count--;

                                    if (count == 0) {
                                        System.out.println("The initial lane " + number +
                                                " has no permits and is moved to the back of the new lane queue");
                                        queue.add(p);
                                    }
                                }

                            }
                        }
                    }
                }
            };
            case "railroad" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    Intersection intersection =
                            IntersectionFactory.getIntersection(handlerType);

                    // cozile pt fiecare sens
                    // le folosim pentru a tine minte ordinea in care masinile au venit
                    ArrayBlockingQueue<Integer> queue0 = intersection.getQueue(0);
                    ArrayBlockingQueue<Integer> queue1 = intersection.getQueue(1);

                    if (car.getStartDirection() == 0) {
                        synchronized (queue0) {
                            System.out.println("Car " + car.getId() +
                                    " from side number 0 has stopped by the railroad");


                                queue0.add(car.getId());

                        }
                    } else {
                        synchronized (queue1) {
                            System.out.println("Car " + car.getId() +
                                    " from side number 1 has stopped by the railroad");

                                queue1.add(car.getId());

                        }
                    }

                    try {
                        Main.barrier.await();

                        if (car.getId() == 0) {
                            System.out.println("The train has passed, cars can now proceed");
                        }

                        Main.barrier.await();

                        // dupa ce a trecut trenul masinile ies in ordinea in care au venit
                        if (car.getStartDirection() == 0) {
                            synchronized (queue0) {
                                int id = queue0.poll();
                                System.out.println("Car " + id +
                                        " from side number 0 has started driving");
                            }
                        } else {
                            synchronized (queue1) {
                                int id = queue1.poll();
                                System.out.println("Car " + id +
                                        " from side number 1 has started driving");
                            }
                        }

                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            };
            default -> null;
        };
    }
}
