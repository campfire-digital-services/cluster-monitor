package au.com.permeance.clusterMonitor.history;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockInvoker {

    private final LockInvoker readLockInvoker;

    private final LockInvoker writeLockInvoker;

    public ReadWriteLockInvoker() {
        this(new ReentrantReadWriteLock());
    }

    public ReadWriteLockInvoker(final ReadWriteLock lock) {
        readLockInvoker = new LockInvoker(lock.readLock());
        writeLockInvoker = new LockInvoker(lock.writeLock());
    }

    public void invokeUnderWriteLock(final Invokable invokable) {
        writeLockInvoker.invoke(invokable);
    }

    public void invokeUnderReadLock(final Invokable invokable) {
        readLockInvoker.invoke(invokable);
    }

}
