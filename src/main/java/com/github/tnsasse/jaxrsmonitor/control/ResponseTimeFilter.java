package com.github.tnsasse.jaxrsmonitor.control;

import com.github.tnsasse.jaxrsmonitor.boundary.MonitoringConfiguration;

import java.io.IOException;
import java.util.logging.Logger;

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
 * @author tnsasse
 */
@WebFilter(filterName = "jaxrs-monitor response time measurement filter", urlPatterns = "/*")
@Provider
@Priority(0)
public class ResponseTimeFilter implements Filter {

  @Inject
  MetricsCollector collector;

  @Inject
  MonitoringConfiguration configuration;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    long startTime = System.currentTimeMillis();
    chain.doFilter(request, response);

    if (configuration.isResponseMetricsEnabled() && request instanceof HttpServletRequest) {
      HttpServletRequest httpRequest = (HttpServletRequest) request;
      final String httpPath = httpRequest.getPathInfo();
      final String httpMethod = httpRequest.getMethod();

      if (httpPath != null && !httpPath.isEmpty() && isPathMonitored(httpPath)) {
        int responseTime = (int) (System.currentTimeMillis() - startTime);
        this.collector.countResponseTime(httpPath, httpMethod, responseTime);
      }
    }
  }

  private boolean isPathMonitored(final String httpPath) {
    if (configuration.getMaxPathDepth() == -1) {
      return true;
    }
    if (configuration.getMaxPathDepth() == 0) {
      return false;
    }

    long pathDepth = httpPath.codePoints().filter(ch -> ch == '/').count();
    return pathDepth <= configuration.getMaxPathDepth();
  }

  @Override
  public void destroy() {
  }

  @Override
  public void init(FilterConfig config) throws ServletException {
  }
}
