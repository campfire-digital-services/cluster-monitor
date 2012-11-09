package au.com.permeance.clusterMonitor.monitor;

import com.liferay.portal.kernel.json.JSONObject;

public interface Monitor {

    String MESSAGE_ATTRIBUTE_MONITOR_NAME = "MESSAGE_ATTRIBUTE_MONITOR_NAME";

    JSONObject poll();

}
