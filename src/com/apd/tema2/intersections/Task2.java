package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Task2 implements Intersection {
    private final int t;
    private final Semaphore sem;

    public Task2(int n, int t) {
        this.t = t;
        this.sem = new Semaphore(n);
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