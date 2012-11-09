package au.com.permeance.clusterMonitor.monitor;

import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.lang.management.CompilationMXBean;

public class CompilationMonitor implements Monitor {

    private final CompilationMXBean compilationMXBean;

    private final JSONFactory jsonFactory;

    public CompilationMonitor(final CompilationMXBean compilationMXBean,
                              final JSONFactory jsonFactory) {
        this.compilationMXBean = compilationMXBean;
        this.jsonFactory = jsonFactory;
    }

    @Override
    public JSONObject poll() {
        return jsonFactory.createJSONObject()
                          .put("totalCompilationTime", compilationMXBean.getTotalCompilationTime());
    }

}
