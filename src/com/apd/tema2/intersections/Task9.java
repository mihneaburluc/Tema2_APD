package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Task9 implements Intersection {
    ArrayBlockingQueue<Integer>[] queue;
    private final int n;
    private final int m;
    private final int x;

    public Task9(int m, int n, int x) {
        this.x = x;
        this.n = n;
        this.m = m;
        queue = new ArrayBlockingQueue[n];

        for (int i = 0; i < n; i++) {
            queue[i] = new ArrayBlockingQueue<>(Main.carsNo);
        }
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
        return m;
    }

    @Override
    public int getN() {
        return n;
    }

    @Override
    public int getX() {
        return x;
    }

    public ArrayBlockingQueue<Integer> getQueue(int i) {
        return queue[i];
    }
}
