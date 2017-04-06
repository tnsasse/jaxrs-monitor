package com.github.tnsasse.jaxrsmonitor.boundary;

import com.github.tnsasse.jaxrsmonitor.control.MetricsCollector;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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

  @Inject
  MonitoringConfiguration configuration;

  @GET
  @Path("/")
  public JsonObject all() {
    JsonObjectBuilder builder = Json.createObjectBuilder();

    if (configuration.isSystemMetricsEnabled()) {
      builder.add("system", system());
    }

    if (configuration.isAppMetricsEnabled()) {
      builder.add("app", app());
    }

    if (configuration.isResponseMetricsEnabled()) {
      builder.add("response", response());
    }

    return builder.build();
  }

  @GET
  @Path("/system")
  public JsonObject system() {
    if (configuration.isSystemMetricsEnabled()) {
      return this.collector.getSystemMetrics().toJson();
    } else {
      return Json.createObjectBuilder().build();
    }
  }

  @GET
  @Path("/app")
  public JsonObject app() {
    if (configuration.isAppMetricsEnabled()) {
      return this.collector.getAppMetrics().toJson();
    } else {
      return Json.createObjectBuilder().build();
    }
  }

  @GET
  @Path("/response")
  public JsonObject response() {
    if (configuration.isResponseMetricsEnabled()) {
      return this.collector.getResponseMetrics().toJson();
    } else {
      return Json.createObjectBuilder().build();
    }
  }
}
