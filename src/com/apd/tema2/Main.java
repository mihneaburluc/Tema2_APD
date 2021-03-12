package com.apd.tema2;

import com.apd.tema2.entities.Pedestrians;
import com.apd.tema2.io.Reader;
import java.util.Set;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static Pedestrians pedestrians = null;
    public static int carsNo;
    public static CyclicBarrier barrier;
    public static volatile AtomicInteger inIntersection = new AtomicInteger(0);

    public static void main(String[] args) {
        Reader fileReader = Reader.getInstance(args[0]);
        Set<Thread> cars = fileReader.getCarsFromInput();
        barrier = new CyclicBarrier(carsNo);

        for(Thread car : cars) {
            car.start();
        }

        if(pedestrians != null) {
            try {
                Thread p = new Thread(pedestrians);
                p.start();
                p.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(Thread car : cars) {
            try {
                car.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
