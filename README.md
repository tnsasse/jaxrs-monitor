[![Build Status](https://travis-ci.org/tnsasse/jaxrs-monitor.svg?branch=master)](https://travis-ci.org/tnsasse/jaxrs-monitor)

# jaxrs-monitor
A tiny monitoring plugin for JavaEE applications. jaxrs-monitor collects and exposes
metrics about the application, its runtime and response times. Metrics are exposed
via a JAX-RS resource in JSON format.

This tool provides easy means to integrate your application into a centralized
monitoring infrastructure.

Make sure that your application server provides JAX-RS, JSON and EJB implementations.
jaxrs-monitor was tested on IBM WebSphere Liberty Profile (webProfile7).

This application is licensed under the BSD 2-clause licence.

## Usage
Include the following in your Maven Pom File:
    
    <dependency>
        <groupId>com.github.tnsasse</groupId>
        <artifactId>jaxrs-monitor</artifactId>
        <version>VERSION_GOES_HERE</version>
    </dependency>

Substitute `VERSION_GOES_HERE` with the current version of your choice.

jaxrs-monitor provides a servlet filter to collect response times and will expose 
metrics via the path `/jaxrs-monitor` so make sure to avoid collisions.

You can configure the following parameters, by calling the `MonitorConfiguration` bean.

  - Max monitoring depth: restrict the path depth that should be monitored for response times metrics.
  - Enable/disable the app,  system and response metrics individually.
  
Example Configuration:

```java
    @Inject
    MonitoringConfiguration configuration
```

Set the desired settings, see the Javadoc for more information:
```java
    configuration.setSystemMetricsEnabled(true);
    configuration.setAppMetricsEnabled(true);
    configuration.setResponseMetricsEnabled(true);
    configuration.setMaxPathDepth(-1);

```
  

## Build / Install
You can clone this repository and build it with maven:

    git clone https://github.com/tnsasse/jaxrs-monitor.git
    cd jaxrs-monitor
    mvn clean install

## Exposed metrics
jaxrs-monitor exposes the following metrics, divided into categories:

### System metrics
  * Number of processors available to the jvm ("cores")
  * Available memory in megabytes ("availableMemory")
  * Used memory in megabytes ("usedMemory")
  * Average system load ("loadAverage") 

#### Application metrics
  * Application startup time in ISO8601 ("startupTime")
  * Uptime since application start in milliseconds ("uptime")

#### HTTP response times
Per HTTP request URI and HTTP method:

  * Number of requests ("count")
  * Minimum response time in milliseconds ("min")
  * Maximum response time in milliseconds ("max")
  * Average response time in milliseconds ("avg")

## Sample application
Check out [jaxrs-monitor-sample](https://github.com/tnsasse/jaxrs-monitor-sample) for a working
demo of the application.

## Sample output

All metrics:

    GET http://URL:PORT/CONTEXT_ROOT/jaxrs-monitor
    {
        "system" : {
            "cores" : 2,
            "availableMemory" : 26,
            "usedMemory" : 49,
            "loadAverage" : 0.65
        },
        "app" : { 
            "startupTime" : "2017-04-01T12:59:01.660Z[UTC]",
            "uptime" : 42648
        },
        "response" : {
            "/orders" : { 
                "GET" : {"count":11,"min":3,"max":18,"avg":7},
                "POST" : {"count":4,"min":23,"max":83,"avg":13}
            },
            "/profile" : {
                "GET" : {"count":14,"min":2,"max":15,"avg":8}
            }
        }
    }

Only system metrics:

    GET http://URL:PORT/CONTEXT_ROOT/jaxrs-monitor/system
    {
        "cores" : 2,
        "availableMemory" : 26,
        "usedMemory" : 49,
        "loadAverage" : 0.65
    }

Only app metrics:

    GET http://URL:PORT/CONTEXT_ROOT/jaxrs-monitor/app
    {
        "startupTime" : "2017-04-01T12:59:01.660Z[UTC]",
        "uptime" : 42648
    }

Only response times:

    GET http://URL_PORT/CONTEXT_ROOT/jaxrs-monitor/response
    {
        "/orders" : { 
            "GET" : {"count":11,"min":3,"max":18,"avg":7},
            "POST" : {"count":4,"min":23,"max":83,"avg":13}
        },
        "/profile" : {
                "GET" : {"count":14,"min":2,"max":15,"avg":8}
        }
    }