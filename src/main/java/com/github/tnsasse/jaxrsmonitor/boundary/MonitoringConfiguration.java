package com.github.tnsasse.jaxrsmonitor.boundary;

import javax.ejb.Singleton;


/**
 * Configures the behaviour of jaxrs-monitor
 *
 * @author tnsasse
 */
@Singleton
public class MonitoringConfiguration {

  private int maxPathDepth = -1;

  private boolean isAppMetricsEnabled = true;
  private boolean isSystemMetricsEnabled = true;
  private boolean isResponseMetricsEnabled = true;

  /**
   * Sets the max path depth for which response metrics are gathered for.
   *
   * The default value of -1 it means that each path will be monitored.
   * If you set it to 0, no path will be monitored.
   *
   * If you set it to N larger than 0, only N levels of path elements
   * under the servlet root will be monitored.
   *
   * Each path that contains more elements will be ignored.
   *
   * Example:
   * A setting of 1 will monitor the path "/order"
   * but will not monitor the path "/order/12"
   *
   * @param depth: levels of path to be inpspected.
   */
  public void setMaxPathDepth(final int depth) {
    this.maxPathDepth = depth;
  }

  public int getMaxPathDepth() {
    return this.maxPathDepth;
  }

  /**
   * Enable/disable the app metrics in the output.
   *
   * The default value is true.
   *
   * @param appMetricsEnabled true means enabled
   */
  public void setAppMetricsEnabled(boolean appMetricsEnabled) {
    this.isAppMetricsEnabled = appMetricsEnabled;
  }

  public boolean isAppMetricsEnabled() {
    return this.isAppMetricsEnabled;
  }

  /**
   * Enable/disable the system metrics in the output.
   *
   * The default value is true.
   *
   * @param systemMetricsEnabled true means enabled
   */
  public void setSystemMetricsEnabled(boolean systemMetricsEnabled) {
    this.isSystemMetricsEnabled = systemMetricsEnabled;
  }

  public boolean isSystemMetricsEnabled() {
    return this.isSystemMetricsEnabled;
  }

  /**
   * Enable/disable the response metrics in the output.
   * This will also enable/disable the HTTP Filter for data collection.
   *
   * The default value is true.
   *
   * @param responseMetricsEnabled true means enabled
   */

  public void setResponseMetricsEnabled(boolean responseMetricsEnabled) {
    this.isResponseMetricsEnabled = responseMetricsEnabled;
  }

  public boolean isResponseMetricsEnabled() {
    return this.isResponseMetricsEnabled;
  }

}
