package au.com.permeance.clusterMonitor.history;

import com.liferay.portal.kernel.json.JSONObject;

import java.util.Map;

public interface MonitorEventListener {

    void onChange(String nodeId,
                  Map<String, ? extends Map<String, ? extends Map<Long, JSONObject>>> history);

    void onChange(String nodeId,
                  String monitorId,
                  Map<String, ? extends Map<String, ? extends Map<Long, JSONObject>>> history);

}
