package au.com.permeance.clusterMonitor.history;

import java.util.concurrent.locks.Lock;

public class LockInvoker {

    private final Lock lock;

    public LockInvoker(final Lock lock) {
        this.lock = lock;
    }

    public void invoke(final Invokable invokable) {
        try {
            lock.lock();
            invokable.invoke();
        }
        finally {
            lock.unlock();
        }
    }

}
