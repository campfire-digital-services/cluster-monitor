package au.com.permeance.clusterMonitor.schedule;

import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.scheduler.SchedulerEngineUtil;
import com.liferay.portal.kernel.scheduler.SchedulerException;
import com.liferay.portal.kernel.scheduler.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

import static au.com.permeance.clusterMonitor.monitor.Monitor.MESSAGE_ATTRIBUTE_MONITOR_NAME;
import static com.liferay.portal.kernel.scheduler.SchedulerEngineUtil.schedule;
import static com.liferay.portal.kernel.scheduler.StorageType.MEMORY;
import static java.lang.String.format;

public class ClusterMonitorSchedulerConfigurator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterMonitorSchedulerConfigurator.class);

    private final String destinationName;

    private final Map<String, ? extends Trigger> triggers;

    public ClusterMonitorSchedulerConfigurator(final String destinationName,
                                               final Map<String, ? extends Trigger> triggers) {
        this.destinationName = destinationName;
        this.triggers = new LinkedHashMap<String, Trigger>(triggers);
    }

    protected void afterPropertiesSet() {
        for (final Map.Entry<String, ? extends Trigger> triggerEntry : triggers.entrySet()) {
            final String monitorName = triggerEntry.getKey();
            final Trigger trigger = triggerEntry.getValue();
            schedule(monitorName, trigger);
        }
    }

    protected void schedule(final String monitorName,
                            final Trigger trigger) {
        LOGGER.info("Scheduling monitor: {} with trigger: {}", monitorName, trigger);
        final Message message = new Message();
        message.put(MESSAGE_ATTRIBUTE_MONITOR_NAME, monitorName);
        final String description = format("Schedule job for monitor: %s", monitorName);
        try {
            SchedulerEngineUtil.schedule(trigger,
                                         MEMORY,
                                         description,
                                         destinationName,
                                         message,
                                         0);
        }
        catch (SchedulerException e) {
            LOGGER.warn("Error scheduling job for monitor: {}", monitorName, e);
        }
    }

    protected void destroy() {
        for (final Trigger trigger : triggers.values()) {
            unschedule(trigger);
        }
    }

    protected void unschedule(final Trigger trigger) {
        LOGGER.info("Unscheduling trigger: {}", trigger);
        try {
            SchedulerEngineUtil.unschedule(trigger.getGroupName(),
                                           trigger.getJobName(),
                                           MEMORY);
        }
        catch (SchedulerException e) {
            LOGGER.warn("Error unscheduling trigger: {}", trigger, e);
        }
    }

}
