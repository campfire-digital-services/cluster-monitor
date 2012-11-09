package au.com.permeance.clusterMonitor.monitor;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class MemoryMonitor implements Monitor {

    private final JSONFactory jsonFactory;

    private final MemoryMXBean memoryMXBean;

    public MemoryMonitor(final JSONFactory jsonFactory,
                         final MemoryMXBean memoryMXBean) {
        this.jsonFactory = jsonFactory;
        this.memoryMXBean = memoryMXBean;
    }

    @Override
    public JSONObject poll() {

        final MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        final MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();

        final JSONObject result = jsonFactory.createJSONObject();

        if (heapMemoryUsage != null) {
            result.put("heap", jsonFactory.createJSONObject()
                                          .put("committed", heapMemoryUsage.getCommitted())
                                          .put("init", heapMemoryUsage.getInit())
                                          .put("max", heapMemoryUsage.getMax())
                                          .put("used", heapMemoryUsage.getUsed()));
        }

        if (nonHeapMemoryUsage != null) {
            result.put("nonHeap", jsonFactory.createJSONObject()
                                             .put("committed", nonHeapMemoryUsage.getCommitted())
                                             .put("init", nonHeapMemoryUsage.getInit())
                                             .put("max", nonHeapMemoryUsage.getMax())
                                             .put("used", nonHeapMemoryUsage.getUsed()));
        }

        return result;
    }

}
