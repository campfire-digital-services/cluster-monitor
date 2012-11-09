package au.com.permeance.clusterMonitor.controller;

import au.com.permeance.clusterMonitor.history.MonitorHistoryProvider;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.portlet.ResourceResponse;
import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;

import static com.liferay.portal.kernel.util.ContentTypes.APPLICATION_JSON;
import static com.liferay.portal.kernel.util.StringUtil.merge;

@Controller
public class ClusterMonitorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterMonitorController.class);

    private final String defaultTab;

    private final JSONSerializer jsonSerializer;

    private final MonitorHistoryProvider monitorHistoryProvider;

    private final Map<String, String> monitorTabs;

    public ClusterMonitorController(final String defaultTab,
                                    final JSONSerializer jsonSerializer,
                                    final MonitorHistoryProvider monitorHistoryProvider,
                                    final Map<String, String> monitorTabs) {
        this.defaultTab = defaultTab;
        this.jsonSerializer = jsonSerializer;
        this.monitorHistoryProvider = monitorHistoryProvider;
        this.monitorTabs = monitorTabs;
    }

    @ModelAttribute("history")
    public SortedMap<String, SortedMap<String, SortedMap<Long, JSONObject>>> clusterNodes() {
        return monitorHistoryProvider.getSortedHistory();
    }

    @ModelAttribute("tabNames")
    public String tabNames() {
        return merge(monitorTabs.values());
    }

    @ModelAttribute("tabValues")
    public String tabValues() {
        return merge(monitorTabs.keySet());
    }

    @RenderMapping(params = "currentTab")
    @RequestMapping("VIEW")
    public String render(@RequestParam("currentTab")
                         final String currentTab) {

        return currentTab;
    }

    @RenderMapping
    @RequestMapping("VIEW")
    public String render() {
        return defaultTab;
    }

    @ResourceMapping
    @RequestMapping(value = "VIEW",
                    params = {"nodeId", "monitorName"})
    public void renderResourceForNodeMonitor(final ResourceResponse response,
                                             @RequestParam("nodeId")
                                             final String nodeId,
                                             @RequestParam("monitorName")
                                             final String monitorName) {

        renderResource(response, monitorHistoryProvider.getSortedHistory(nodeId, monitorName));
    }

    protected void renderResource(final ResourceResponse response,
                                  final Object object) {

        final String json = jsonSerializer.serializeDeep(object);

        response.setContentType(APPLICATION_JSON);
        response.setContentLength(json.length());

        try {
            response.getWriter().write(json);
        }
        catch (final IOException e) {
            LOGGER.error("Error writing json response: {}", json, e);
        }
    }

}
