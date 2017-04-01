package com.github.tnsasse.jaxrsmonitor.control;

import java.io.IOException;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ext.Provider;

/**
 * A filter to measure response times
 *
 * @author tnsasse
 */
@WebFilter(filterName = "jaxrs-monitor response time measurement filter", urlPatterns = "/*")
@Provider
@Priority(0)
public class ResponseTimeFilter implements Filter {

    @Inject
    MetricsCollector collector;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean isHttpRequest = false;
        long startTime = System.currentTimeMillis();

        String httpPath = null;
        String httpMethod = null;

        if (request instanceof HttpServletRequest) {
            isHttpRequest = true;
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            httpPath = httpRequest.getPathInfo();
            httpMethod = httpRequest.getMethod();
        }

        chain.doFilter(request, response);

        if (isHttpRequest) {
            if(httpPath != null && !httpPath.isEmpty()) {
                int responseTime = (int) (System.currentTimeMillis() - startTime);
                this.collector.countResponseTime(httpPath, httpMethod, responseTime);
            }
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }
}
