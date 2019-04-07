package com.https.connectivity;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConnectionConfig {

    @Value("")
    private int connectionTimeout;

    @Value("")
    private int requestTimeout;

    @Value("")
    private int socketTimeout;

    @Autowired
    private ConnectionPool connectionPool;

    @Bean(name = "httpClient", destroyMethod = "close")
    public CloseableHttpClient newInstance() {

        RequestConfig requestConfig = requestConfigBuilder();
        return closableHttpClientBuilder(requestConfig);
    }

    private CloseableHttpClient closableHttpClientBuilder(RequestConfig requestConfig) {
        return HttpClients.custom().
                setDefaultRequestConfig(requestConfig).
                setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy()).
                setConnectionManagerShared(true).
                setConnectionManager(connectionPool.getPoolingHttpClientConnectionManager()).
                build();
    }

    private RequestConfig requestConfigBuilder() {
        return RequestConfig.custom().
                setConnectionRequestTimeout(connectionTimeout).
                setConnectTimeout(connectionTimeout).
                setSocketTimeout(socketTimeout).
                build();
    }
}
