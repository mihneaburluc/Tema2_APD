package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Task10 implements Intersection {
    ArrayBlockingQueue<Integer> queue0;
    ArrayBlockingQueue<Integer> queue1;


    public Task10() {
        queue0 = new ArrayBlockingQueue<>(Main.carsNo);
        queue1 = new ArrayBlockingQueue<>(Main.carsNo);
    }

    @Override
    public Semaphore getSemaphore(int i) {
        return null;
    }


    @Override
    public CyclicBarrier getBarrier() {
        return null;
    }

    public int getT() {
        return 0;
    }

    @Override
    public int getN() {
        return 0;
    }

    @Override
    public int getX() {
        return 0;
    }

    public ArrayBlockingQueue<Integer> getQueue(int i) {
        if (i == 0) {
            return queue0;
        }
        return queue1;
    }

}