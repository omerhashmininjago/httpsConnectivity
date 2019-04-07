package com.https.connectivity;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Component
public class ConnectionPool {

    @Value("")
    private int maxTotalConnections;

    @Value("")
    private int defaultMaxConnectionsPerRoute;

    @Value("")
    private int closeIdleConnectionWaitTime;

    @Value("")
    private int dropStaleConnectionTimeout;

    private volatile PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = null;

    @PostConstruct
    private void initializeConnectionPool() {
        poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxConnectionsPerRoute);
        poolingHttpClientConnectionManager.setMaxTotal(maxTotalConnections);
    }

    @PreDestroy
    private void shutDownConnectionPool() {
        poolingHttpClientConnectionManager.shutdown();
        poolingHttpClientConnectionManager = null;
    }

    @Scheduled(cron = "")
    public void clearStaleConnections() {
        if (poolingHttpClientConnectionManager != null) {
            poolingHttpClientConnectionManager.closeExpiredConnections();
            poolingHttpClientConnectionManager.closeIdleConnections(closeIdleConnectionWaitTime, TimeUnit.SECONDS);
        }
    }

    public PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager() {
        if (poolingHttpClientConnectionManager == null) {
            throw new IllegalStateException("Connection Pool is not initialized, so issue");
        }
        return poolingHttpClientConnectionManager;
    }
}
