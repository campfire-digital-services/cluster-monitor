package au.com.permeance.clusterMonitor.history;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitorTrackerMessageListener extends BaseMessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorTrackerMessageListener.class);

    private final MonitoryHistoryWriter monitoryHistoryWriter;

    public MonitorTrackerMessageListener(final MonitoryHistoryWriter monitoryHistoryWriter) {
        this.monitoryHistoryWriter = monitoryHistoryWriter;
    }

    @Override
    protected void doReceive(final Message message) throws Exception {
        if (checkMessagePayload(message)) {
            final MessageStatus messageStatus = (MessageStatus) message.getPayload();

            if (checkMessageStatusPayload(messageStatus)) {
                final JSONObject jsonObject = (JSONObject) messageStatus.getPayload();

                if (checkJSONObject(jsonObject, "nodeId")
                    && checkJSONObject(jsonObject, "monitorName")
                    && checkJSONObject(jsonObject, "timestamp")
                    && checkJSONObject(jsonObject, "result")) {

                    final String nodeId = jsonObject.getString("nodeId");
                    final String monitorName = jsonObject.getString("monitorName");
                    final Long timestamp = jsonObject.getLong("timestamp");
                    final JSONObject result = jsonObject.getJSONObject("result");

                    monitoryHistoryWriter.addHistoryItem(nodeId,
                                                  monitorName,
                                                  timestamp,
                                                  result);
                }
            }
        }
    }

    protected boolean checkMessagePayload(final Message message) {
        if (!(message.getPayload() instanceof MessageStatus)) {
            LOGGER.warn("Received a Message which doesn't contain a MessageStatus: {}", message.getPayload());
            return false;
        }
        return true;
    }

    protected boolean checkMessageStatusPayload(final MessageStatus messageStatus) {
        if (!(messageStatus.getPayload() instanceof JSONObject)) {
            LOGGER.warn("Received a MessageStatus which doesn't contain a JSONObject: {}", messageStatus.getPayload());
            return false;
        }
        return true;
    }

    protected boolean checkJSONObject(final JSONObject jsonObject, final String key) {
        if (!jsonObject.has(key)) {
            LOGGER.warn("Received a JSONObject: {} which doesn't contain a field named: {}", jsonObject, key);
            return false;
        }
        return true;
    }

}
