package com.apd.tema2.utils;

import java.util.concurrent.ArrayBlockingQueue;

public class Pair {
    public ArrayBlockingQueue<Integer> queue;
    public int n;

    public Pair(ArrayBlockingQueue<Integer> queue, int n) {
        this.queue = queue;
        this.n = n;
    }

}
