package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Task5 implements Intersection {

    private final int t;
    private final Semaphore[] sem;

    public Task5(int n, int t, int x) {
        this.t = t;
        this.sem = new Semaphore[n];
        for (int i = 0 ; i < n; i++) {
            sem[i] = new Semaphore(x);
        }
    }

    @Override
    public Semaphore getSemaphore(int i) {
        return sem[i];
    }


    @Override
    public CyclicBarrier getBarrier() {
        return null;
    }

    public int getT() {
        return t;
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
