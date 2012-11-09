package au.com.permeance.clusterMonitor.history;

import com.liferay.portal.kernel.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SizeMonitorEventListener extends BaseMonitorEventListener {

    private final Integer defaultSize;

    private final Map<String, Integer> monitorSizes;

    public SizeMonitorEventListener(final Integer defaultSize,
                                    final Map<String, Integer> monitorSizes) {
        this.defaultSize = defaultSize;
        this.monitorSizes = new HashMap<String, Integer>(monitorSizes);
    }

    @Override
    public void onChange(final String nodeId,
                         final String monitorName,
                         final Map<String, ? extends Map<String, ? extends Map<Long, JSONObject>>> history) {

        final Integer size = monitorSizes.containsKey(monitorName)
                             ? monitorSizes.get(monitorName)
                             : defaultSize;

        while (history.get(nodeId)
                      .get(monitorName)
                      .size() > size) {

            Long oldest = null;
            for (final Long timestamp : history.get(nodeId)
                                               .get(monitorName)
                                               .keySet()) {
                if (oldest == null || timestamp < oldest) {
                    oldest = timestamp;
                }
            }

            if (oldest != null) {
                history.get(nodeId)
                       .get(monitorName)
                       .remove(oldest);
            }
        }
    }

}
