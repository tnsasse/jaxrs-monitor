package net.byte23.jaxrsmonitor.entity;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Holds metrics about the application runtime
 *
 * @author tnsasse
 */
public class SystemMetrics {

    final private int cores;
    final private long availableMemory;
    final private long usedMemory;
    final private double loadAverage;

    public SystemMetrics(int cores, long availableMemory, long usedMemory, double loadAverage) {
        this.cores = cores;
        this.availableMemory = availableMemory;
        this.usedMemory = usedMemory;
        this.loadAverage = loadAverage;
    }

    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("cores", this.cores);
        builder.add("availableMemory", this.availableMemory);
        builder.add("usedMemory", this.usedMemory);
        builder.add("loadAverage", this.loadAverage);
        return builder.build();
    }
}