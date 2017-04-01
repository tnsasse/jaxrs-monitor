package com.github.tnsasse.jaxrsmonitor.entity;

import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Contains a collection of IntSummaryStatistics with a named key
 *
 * @author tnsasse
 */
public class NamedIntSummaryStatistics {

    private final Map<String, IntSummaryStatistics> namedStats;

    public NamedIntSummaryStatistics() {
        this.namedStats = new HashMap<>();
    }

    public void addDataPoint(final String key, final Integer dataPoint) {
        if (!this.namedStats.containsKey(key)) {
            this.namedStats.put(key, new IntSummaryStatistics());
        }

        this.namedStats.get(key).accept(dataPoint);
    }

    public JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        namedStats.keySet().forEach((name) -> {
            IntSummaryStatistics stats = namedStats.get(name);

            JsonObjectBuilder mStatsBuilder = Json.createObjectBuilder();
            mStatsBuilder.add("count", stats.getCount());
            mStatsBuilder.add("min", stats.getMin());
            mStatsBuilder.add("max", stats.getMax());
            mStatsBuilder.add("avg", (int) stats.getAverage());

            builder.add(name, mStatsBuilder.build());
        });

        return builder.build();
    }
}
