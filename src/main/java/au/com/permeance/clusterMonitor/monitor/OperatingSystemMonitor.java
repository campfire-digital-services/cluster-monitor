package au.com.permeance.clusterMonitor.monitor;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.lang.management.OperatingSystemMXBean;

public class OperatingSystemMonitor implements Monitor {

    private final JSONFactory jsonFactory;

    private final OperatingSystemMXBean operatingSystemMXBean;

    public OperatingSystemMonitor(final JSONFactory jsonFactory,
                                  final OperatingSystemMXBean operatingSystemMXBean) {
        this.jsonFactory = jsonFactory;
        this.operatingSystemMXBean = operatingSystemMXBean;
    }

    @Override
    public JSONObject poll() {
        return jsonFactory.createJSONObject()
                          .put("arch", operatingSystemMXBean.getArch())
                          .put("availableProcessors", operatingSystemMXBean.getAvailableProcessors())
                          .put("name", operatingSystemMXBean.getName())
                          .put("systemLoadAverage", operatingSystemMXBean.getSystemLoadAverage())
                          .put("version", operatingSystemMXBean.getVersion());
    }

}
