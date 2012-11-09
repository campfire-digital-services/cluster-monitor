package au.com.permeance.clusterMonitor.monitor;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.lang.management.ClassLoadingMXBean;

public class ClassLoadingMonitor implements Monitor {

    private final ClassLoadingMXBean classLoadingMXBean;

    private final JSONFactory jsonFactory;

    public ClassLoadingMonitor(final ClassLoadingMXBean classLoadingMXBean,
                               final JSONFactory jsonFactory) {
        this.classLoadingMXBean = classLoadingMXBean;
        this.jsonFactory = jsonFactory;
    }

    @Override
    public JSONObject poll() {
        return jsonFactory.createJSONObject()
                          .put("loadedClassCount", classLoadingMXBean.getLoadedClassCount())
                          .put("totalLoadedClassCount", classLoadingMXBean.getTotalLoadedClassCount());
    }

}
