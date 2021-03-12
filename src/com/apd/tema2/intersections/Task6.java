package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Task6 implements Intersection {

    private final Semaphore sem;

    public Task6() {
        this.sem = new Semaphore(1);
    }

    @Override
    public Semaphore getSemaphore(int i) {
        return sem;
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

    @Override
    public ArrayBlockingQueue<Integer> getQueue(int i) {
        return null;
    }

}
