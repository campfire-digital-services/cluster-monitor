package au.com.permeance.clusterMonitor.monitor;

import com.liferay.portal.kernel.cluster.ClusterNode;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.messaging.BaseMessageStatusMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

import static au.com.permeance.clusterMonitor.monitor.Monitor.MESSAGE_ATTRIBUTE_MONITOR_NAME;
import static com.liferay.portal.kernel.util.StringPool.BLANK;
import static com.liferay.portal.kernel.util.StringPool.TILDE;
import static java.lang.System.currentTimeMillis;

public class MonitorDispatcher extends BaseMessageStatusMessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorDispatcher.class);

    private final ClusterNode clusterNode;

    private final JSONFactory jsonFactory;

    private final Map<String, ? extends Monitor> monitors;

    public MonitorDispatcher(final ClusterNode clusterNode,
                             final JSONFactory jsonFactory,
                             final Map<String, ? extends Monitor> monitors) {
        this.clusterNode = clusterNode;
        this.jsonFactory = jsonFactory;
        this.monitors = new LinkedHashMap<String, Monitor>(monitors);
    }

    @Override
    protected void doReceive(final Message message,
                             final MessageStatus messageStatus) throws Exception {

        final String monitorName = message.getString(MESSAGE_ATTRIBUTE_MONITOR_NAME);
        if (!monitors.containsKey(monitorName)) {
            LOGGER.warn("Unknown monitor name: {}", monitorName);
            throw new NoSuchMonitorException(monitorName);
        }

        final String nodeId = clusterNode != null
                              ? clusterNode.getClusterNodeId()
                              : TILDE;

        final JSONObject result = monitors.get(monitorName)
                                          .poll();

        final JSONObject payload = jsonFactory.createJSONObject()
                                              .put("monitorName", monitorName)
                                              .put("nodeId", nodeId)
                                              .put("timestamp", currentTimeMillis())
                                              .put("result", result);

        messageStatus.setPayload(payload);
    }

}
