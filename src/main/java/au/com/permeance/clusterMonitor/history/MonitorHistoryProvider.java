package au.com.permeance.clusterMonitor.history;

import com.liferay.portal.kernel.json.JSONObject;

import java.util.SortedMap;

public interface MonitorHistoryProvider {

    SortedMap<String, SortedMap<String, SortedMap<Long, JSONObject>>> getSortedHistory();

    SortedMap<String, SortedMap<Long, JSONObject>> getSortedHistory(final String nodeId);

    SortedMap<Long, JSONObject> getSortedHistory(String nodeId,
                                                 String monitorName);

}
