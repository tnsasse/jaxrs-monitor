package com.github.tnsasse.jaxrsmonitor.control;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import com.github.tnsasse.jaxrsmonitor.entity.AppMetrics;
import com.github.tnsasse.jaxrsmonitor.entity.NamedIntSummaryStatistics;
import com.github.tnsasse.jaxrsmonitor.entity.ResponseMetrics;
import com.github.tnsasse.jaxrsmonitor.entity.SystemMetrics;

/**
 * Gathers metrics about the application and runtime
 *
 * @author tnsasse
 */
@Startup
@Singleton
public class MetricsCollector {

    private ZonedDateTime startupTime;
    private MemoryMXBean memoryBean;
    private OperatingSystemMXBean osBean;

    private Map<String, NamedIntSummaryStatistics> responseTimesByPath;

    @PostConstruct
    public void init() {
        this.startupTime = ZonedDateTime.now();
        this.memoryBean = ManagementFactory.getMemoryMXBean();
        this.osBean = ManagementFactory.getOperatingSystemMXBean();
        this.responseTimesByPath = new HashMap<>();
    }

    public SystemMetrics getSystemMetrics() {
        return new SystemMetrics(getCores(), getAvailableMemory(), getUsedMemory(), getLoadAverage());
    }

    public AppMetrics getAppMetrics() {
        return new AppMetrics(getStartupTime(), getUptime());
    }

    public ResponseMetrics getResponseMetrics() {
        return new ResponseMetrics(this.responseTimesByPath);
    }

    public void countResponseTime(final String httpPath, final String httpMethod, final Integer responseTime) {
        if (!this.responseTimesByPath.containsKey(httpPath)) {
            this.responseTimesByPath.put(httpPath, new NamedIntSummaryStatistics());
        }

        this.responseTimesByPath.get(httpPath).addDataPoint(httpMethod, responseTime);
    }

    private ZonedDateTime getStartupTime() {
        return this.startupTime;
    }

    private long getUptime() {
        return ChronoUnit.MILLIS.between(getStartupTime(), ZonedDateTime.now());
    }

    private long getAvailableMemory() {
        MemoryUsage currentUsage = this.memoryBean.getHeapMemoryUsage();
        long available = (currentUsage.getCommitted() - currentUsage.getUsed());
        return toMegabyte(available);
    }

    private long getUsedMemory() {
        MemoryUsage currentUsage = this.memoryBean.getHeapMemoryUsage();
        return toMegabyte(currentUsage.getUsed());
    }

    private int getCores() {
        return osBean.getAvailableProcessors();
    }

    private double getLoadAverage() {
        return osBean.getSystemLoadAverage();
    }

    private long toMegabyte(final long bytes) {
        return bytes / 1024 / 1024;
    }
}
