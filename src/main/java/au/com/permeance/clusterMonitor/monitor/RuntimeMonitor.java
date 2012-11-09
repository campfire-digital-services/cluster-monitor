package au.com.permeance.clusterMonitor.monitor;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.lang.management.RuntimeMXBean;
import java.util.Map;

public class RuntimeMonitor implements Monitor {

    private final JSONFactory jsonFactory;

    private final RuntimeMXBean runtimeMXBean;

    public RuntimeMonitor(final JSONFactory jsonFactory,
                          final RuntimeMXBean runtimeMXBean) {
        this.jsonFactory = jsonFactory;
        this.runtimeMXBean = runtimeMXBean;
    }

    @Override
    public JSONObject poll() {

        final JSONArray inputArguments = jsonFactory.createJSONArray();
        for (final String inputArgument : runtimeMXBean.getInputArguments()) {
            inputArguments.put(inputArgument);
        }

        final JSONObject systemProperties = jsonFactory.createJSONObject();
        for (final Map.Entry<String, String> entry : runtimeMXBean.getSystemProperties()
                                                                  .entrySet()) {
            systemProperties.put(entry.getKey(), entry.getValue());
        }

        return jsonFactory.createJSONObject()
                          .put("bootClassPath", runtimeMXBean.getBootClassPath())
                          .put("classPath", runtimeMXBean.getClassPath())
                          .put("inputArguments", inputArguments)
                          .put("libraryPath", runtimeMXBean.getLibraryPath())
                          .put("managementSpecVersion", runtimeMXBean.getManagementSpecVersion())
                          .put("name", runtimeMXBean.getName())
                          .put("specName", runtimeMXBean.getSpecName())
                          .put("specVendor", runtimeMXBean.getSpecVendor())
                          .put("specVersion", runtimeMXBean.getSpecVersion())
                          .put("startTime", runtimeMXBean.getStartTime())
                          .put("systemProperties", systemProperties)
                          .put("uptime", runtimeMXBean.getUptime())
                          .put("vmName", runtimeMXBean.getVmName())
                          .put("vmVendor", runtimeMXBean.getVmVendor())
                          .put("version", runtimeMXBean.getVmVersion());
    }

}
