package au.com.permeance.clusterMonitor.monitor;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.lang.management.GarbageCollectorMXBean;
import java.util.List;

public class GarbageCollectorMonitor implements Monitor {

    private final List<GarbageCollectorMXBean> garbageCollectorMXBeans;

    private final JSONFactory jsonFactory;

    public GarbageCollectorMonitor(final List<GarbageCollectorMXBean> garbageCollectorMXBeans,
                                   final JSONFactory jsonFactory) {
        this.garbageCollectorMXBeans = garbageCollectorMXBeans;
        this.jsonFactory = jsonFactory;
    }

    @Override
    public JSONObject poll() {
        final JSONObject jsonObject = jsonFactory.createJSONObject();

        for (final GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMXBeans) {
            jsonObject.put(garbageCollectorMXBean.getName(), jsonFactory.createJSONObject()
                                                                        .put("collectionCount", garbageCollectorMXBean.getCollectionCount())
                                                                        .put("collectionTime", garbageCollectorMXBean.getCollectionTime()));
        }

        return jsonObject;
    }

}
