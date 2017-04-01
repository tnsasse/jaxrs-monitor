/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.tnsasse.jaxrsmonitor.entity;

import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Holds metrics about the HTTP response times
 *
 * @author tnsasse
 */
public class ResponseMetrics {

    final private Map<String, NamedIntSummaryStatistics> responseTimes;

    public ResponseMetrics(Map<String, NamedIntSummaryStatistics> responseTimes) {
        this.responseTimes = responseTimes;
    }

    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        responseTimes.keySet().forEach((path) -> {
            builder.add(path, responseTimes.get(path).toJson());
        });

        return builder.build();
    }

}
