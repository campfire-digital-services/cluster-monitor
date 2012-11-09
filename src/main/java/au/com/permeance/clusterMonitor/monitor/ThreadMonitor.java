package au.com.permeance.clusterMonitor.monitor;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.lang.management.ThreadMXBean;

public class ThreadMonitor implements Monitor {

    private final JSONFactory jsonFactory;

    private final ThreadMXBean threadMXBean;

    public ThreadMonitor(final JSONFactory jsonFactory,
                         final ThreadMXBean threadMXBean) {
        this.jsonFactory = jsonFactory;
        this.threadMXBean = threadMXBean;
    }

    @Override
    public JSONObject poll() {
        return jsonFactory.createJSONObject()
                          .put("daemonThreadCount", threadMXBean.getDaemonThreadCount())
                          .put("peakThreadCount", threadMXBean.getPeakThreadCount())
                          .put("threadCount", threadMXBean.getThreadCount())
                          .put("totalStartedThreadCount", threadMXBean.getTotalStartedThreadCount());
    }

}
