package au.com.permeance.clusterMonitor.history;

import com.liferay.portal.kernel.json.JSONObject;

public interface MonitoryHistoryWriter {

    void addHistoryItem(String nodeId,
                        String monitorName,
                        Long timestamp,
                        JSONObject result);

}
