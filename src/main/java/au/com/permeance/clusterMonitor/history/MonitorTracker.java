package au.com.permeance.clusterMonitor.history;

import com.liferay.portal.kernel.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MonitorTracker implements MonitorHistoryProvider,
                                       MonitoryHistoryWriter {

    private final ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<Long, JSONObject>>> history;

    private final ReadWriteLockInvoker readWriteLockInvoker;

    private final List<MonitorEventListener> eventListeners;

    public MonitorTracker(final List<MonitorEventListener> eventListeners,
                          final ReadWriteLockInvoker readWriteLockInvoker) {

        this.history = new ConcurrentHashMap<String, ConcurrentMap<String, ConcurrentMap<Long, JSONObject>>>();

        this.readWriteLockInvoker = readWriteLockInvoker;
        this.eventListeners = new ArrayList<MonitorEventListener>(eventListeners);
    }

    protected void notifyListeners(final String nodeId,
                                   final String monitorName) {
        readWriteLockInvoker.invokeUnderWriteLock(new Invokable() {
            @Override
            public void invoke() {
                for (final MonitorEventListener eventListener : eventListeners) {
                    eventListener.onChange(nodeId, monitorName, history);
                }
            }
        });
    }

    protected void notifyListeners(final String nodeId) {
        readWriteLockInvoker.invokeUnderWriteLock(new Invokable() {
            @Override
            public void invoke() {
                for (final MonitorEventListener eventListener : eventListeners) {
                    eventListener.onChange(nodeId, history);
                }
            }
        });
    }

    @Override
    public void addHistoryItem(final String nodeId,
                               final String monitorName,
                               final Long timestamp,
                               final JSONObject result) {
        readWriteLockInvoker.invokeUnderWriteLock(new Invokable() {
            @Override
            public void invoke() {

                history.putIfAbsent(nodeId, new ConcurrentHashMap<String, ConcurrentMap<Long, JSONObject>>());

                history.get(nodeId)
                       .putIfAbsent(monitorName, new ConcurrentHashMap<Long, JSONObject>());

                history.get(nodeId)
                       .get(monitorName)
                       .put(timestamp, result);

                notifyListeners(nodeId, monitorName);
                notifyListeners(nodeId);
            }
        });
    }

    @Override
    public SortedMap<String, SortedMap<String, SortedMap<Long, JSONObject>>> getSortedHistory() {
        final SortedMap<String, SortedMap<String, SortedMap<Long, JSONObject>>> copy = new TreeMap<String, SortedMap<String, SortedMap<Long, JSONObject>>>();
        readWriteLockInvoker.invokeUnderReadLock(new Invokable() {
            @Override
            public void invoke() {
                for (final String nodeId : history.keySet()) {
                    final SortedMap<String, SortedMap<Long, JSONObject>> nodeHistory = getSortedHistory(nodeId);
                    copy.put(nodeId, nodeHistory);
                }
            }
        });
        return copy;
    }

    @Override
    public SortedMap<String, SortedMap<Long, JSONObject>> getSortedHistory(final String nodeId) {
        final TreeMap<String, SortedMap<Long, JSONObject>> nodeHistory = new TreeMap<String, SortedMap<Long, JSONObject>>();
        readWriteLockInvoker.invokeUnderReadLock(new Invokable() {
            @Override
            public void invoke() {
                for (final String monitorName : history.get(nodeId)
                                                       .keySet()) {
                    final SortedMap<Long, JSONObject> monitorHistory = getSortedHistory(nodeId, monitorName);
                    nodeHistory.put(monitorName, monitorHistory);
                }
            }
        });
        return nodeHistory;
    }

    @Override
    public SortedMap<Long, JSONObject> getSortedHistory(final String nodeId,
                                                        final String monitorName) {
        final TreeMap<Long, JSONObject> monitorHistory = new TreeMap<Long, JSONObject>();
        readWriteLockInvoker.invokeUnderReadLock(new Invokable() {
            @Override
            public void invoke() {
                for (final Long timestamp : history.get(nodeId)
                                                   .get(monitorName)
                                                   .keySet()) {
                    monitorHistory.put(timestamp, history.get(nodeId)
                                                         .get(monitorName)
                                                         .get(timestamp));
                }
            }
        });
        return monitorHistory;
    }

}
