package au.com.permeance.clusterMonitor.history;

import com.liferay.portal.kernel.json.JSONObject;

import java.util.Map;

public class BaseMonitorEventListener implements MonitorEventListener {

    @Override
    public void onChange(final String nodeId,
                         final Map<String, ? extends Map<String, ? extends Map<Long, JSONObject>>> history) {
        // Does nothing
    }

    @Override
    public void onChange(final String nodeId,
                         final String monitorName,
                         final Map<String, ? extends Map<String, ? extends Map<Long, JSONObject>>> history) {
        // Does nothing
    }

}
