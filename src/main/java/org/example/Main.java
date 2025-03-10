package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<ReentrantLock> lockList = new ArrayList<>();

        lockList.add(new ReentrantLock());
        lockList.add(new ReentrantLock());
        lockList.add(new ReentrantLock());
        lockList.add(new ReentrantLock());
        lockList.add(new ReentrantLock());

        CustomThread customThread = new CustomThread(lockList, 10, 5);
        CustomThread customThread2 = new CustomThread(lockList, 5, 2);

        executor.submit(customThread);
        executor.submit(customThread2);
    }
}