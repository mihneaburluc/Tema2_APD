package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Task8 implements Intersection {

    private final Semaphore[] sem = new Semaphore[2];
    private final CyclicBarrier barrier;

    public Task8(int x) {
        sem[0] = new Semaphore(x);
        sem[1] = new Semaphore(0);
        barrier = new CyclicBarrier(x);
    }

    @Override
    public Semaphore getSemaphore(int i) {
        return sem[i];
    }


    @Override
    public CyclicBarrier getBarrier() {
        return barrier;
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
