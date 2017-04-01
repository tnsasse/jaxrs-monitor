package com.github.tnsasse.jaxrsmonitor.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.github.tnsasse.jaxrsmonitor.control.MetricsCollector;

/**
 * Exposes metrics about the application and runtime via JAX-RS
 *
 * @author tnsasse
 */
@Path("/jaxrs-monitor")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
public class MonitoringResource {

    @Inject
    MetricsCollector collector;

    @GET
    @Path("/")
    public JsonObject all() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("system", this.collector.getSystemMetrics().toJson());
        builder.add("app", this.collector.getAppMetrics().toJson());
        builder.add("response", this.collector.getResponseMetrics().toJson());
        return builder.build();
    }

    @GET
    @Path("/system")
    public JsonObject system() {
        return this.collector.getSystemMetrics().toJson();
    }

    @GET
    @Path("/app")
    public JsonObject app() {
        return this.collector.getAppMetrics().toJson();
    }

    @GET
    @Path("/response")
    public JsonObject response() {
        return this.collector.getResponseMetrics().toJson();
    }
}
