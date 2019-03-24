package com.epam.currency.logic;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Controller {
    private boolean isFree;

    private static Lock lock = new ReentrantLock();
    private static AtomicBoolean initialized = new AtomicBoolean(false);
    private static Controller instance;

    private Controller(boolean isFree) {
        this.isFree = isFree;
    }

    public static Controller getInstance() {
        if(!initialized.get()){
            try {
                lock.lock();
                if (instance == null) {
                    instance = new Controller(true);
                    initialized.set(true);
                }
            }finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public void setFree() {
        isFree = true;
    }

    public void setOccupied() {
        lock.lock();
        try {
            isFree = false;
        } finally {
            lock.unlock();
        }
    }

    public boolean isFree() {
        lock.lock();
        try {
            return isFree;
        } finally {
            lock.unlock();
        }
    }
}
