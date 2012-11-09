package au.com.permeance.clusterMonitor.history;

import com.liferay.portal.kernel.json.JSONObject;

import java.util.Map;

public class PruneMonitorEventListener extends BaseMonitorEventListener {

    @Override
    public void onChange(final String nodeId,
                         final Map<String, ? extends Map<String, ? extends Map<Long, JSONObject>>> history) {

        if (history.get(nodeId).isEmpty()) {
            history.remove(nodeId);
        }
    }

    @Override
    public void onChange(final String nodeId,
                         final String monitorName,
                         final Map<String, ? extends Map<String, ? extends Map<Long, JSONObject>>> history) {
        if (history.get(nodeId).get(monitorName).isEmpty()) {
            history.get(nodeId).remove(monitorName);
        }
    }

}
