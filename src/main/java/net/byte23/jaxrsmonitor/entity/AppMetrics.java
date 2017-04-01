package net.byte23.jaxrsmonitor.entity;

import java.time.ZonedDateTime;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Holds metrics about the application
 * 
 * @author tnsasse
 */
public class AppMetrics {

    final private ZonedDateTime startupTime;
    final private long uptime;

    public AppMetrics(ZonedDateTime startupTime, long uptime) {
        this.startupTime = startupTime;
        this.uptime = uptime;
    }

    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("startupTime", this.startupTime.toString());
        builder.add("uptime", this.uptime);
        return builder.build();
    }

}
