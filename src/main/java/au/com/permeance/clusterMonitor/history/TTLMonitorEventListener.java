package au.com.permeance.clusterMonitor.history;

import com.liferay.portal.kernel.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.lang.System.currentTimeMillis;

public class TTLMonitorEventListener extends BaseMonitorEventListener {

    private final Long defaultTTL;

    private final Map<String, Long> monitorTTLs;

    public TTLMonitorEventListener(final Long defaultTTL,
                                   final Map<String, Long> monitorTTLs) {
        this.defaultTTL = defaultTTL;
        this.monitorTTLs = new HashMap<String, Long>(monitorTTLs);
    }

    @Override
    public void onChange(final String nodeId,
                         final String monitorName,
                         final Map<String, ? extends Map<String, ? extends Map<Long, JSONObject>>> history) {

        final Long ttl = monitorTTLs.containsKey(monitorName)
                         ? monitorTTLs.get(monitorName)
                         : defaultTTL;

        for (final Long timestamp : history.get(nodeId)
                                           .get(monitorName)
                                           .keySet()) {

            if (ttl < currentTimeMillis() - timestamp) {
                history.get(nodeId)
                       .get(monitorName)
                       .remove(timestamp);
            }
        }
    }

}
