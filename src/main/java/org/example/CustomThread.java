package org.example;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class CustomThread implements Runnable {
    private List<ReentrantLock> locks;
    private int periodSec;
    private int workDurationSec;

    public CustomThread(List<ReentrantLock> locks, int periodSec, int workDurationSec ) {
        this.locks = locks;
        this.periodSec = periodSec;
        this.workDurationSec = workDurationSec;
    }

    @Override
    public void run() {
        while (true) {
            int lockSize = locks.size();
            int lockOneIndx = getRandomNumber(0, lockSize);
            int lockTwoIndx = getRandomNumber(0, lockSize);
            while (lockOneIndx == lockTwoIndx) {
                lockTwoIndx = getRandomNumber(0, lockSize);
            }

            ReentrantLock lock1 = locks.get(lockOneIndx);
            ReentrantLock lock2 = locks.get(lockTwoIndx);

            if (lockAttempt(lock1, lock2)){
                try {
                    System.out.println(Thread.currentThread().getName() + ": Doing some work for " + workDurationSec + " seconds");
                    Thread.sleep(workDurationSec* 1000L);
                    System.out.println(Thread.currentThread().getName() + ": Done work in thread ");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    System.out.println(Thread.currentThread().getName() + ": Releasing locks after work in thread ");
                   lock1.unlock();
                   lock2.unlock();
                }
            }

            try {
                System.out.println(Thread.currentThread().getName() + ": Waiting for next fire time for " + workDurationSec + " seconds");
                Thread.sleep(periodSec*1000L);
                System.out.println(Thread.currentThread().getName() + ": Next fire time now");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private boolean lockAttempt(ReentrantLock lock1, ReentrantLock lock2) {
        System.out.println(Thread.currentThread().getName() + ": Truing aqcuire locks");
        boolean lockOneLocked = lock1.tryLock();
        boolean lockTwoLocked = lock2.tryLock();
        if (lockOneLocked && lockTwoLocked) {
            System.out.println(Thread.currentThread().getName() + ": Success locking in Thread");
            return true;
        }else {
            System.out.println(Thread.currentThread().getName() + ": Can`t get both locks, releasing locks in Thread");
            if (lockOneLocked){
                lock1.unlock();
            }
            if (lockTwoLocked){
                lock2.unlock();
            }
            return false;
        }
    }
}
