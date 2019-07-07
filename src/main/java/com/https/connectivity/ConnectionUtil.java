package com.https.connectivity;

import com.details.spec.ConnectivityDetails;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConnectionUtil {

    @Value("")
    private int connectionTimeout;

    @Value("")
    private int requestTimeout;

    @Value("")
    private int socketTimeout;

    public HttpClientContext getContext(final ConnectivityDetails connectivityDetails) {

        HttpClientContext httpClientContext = HttpClientContext.create();

        CredentialsProvider credentialsProvider = getCredentialsProvider(connectivityDetails);
        Registry<AuthSchemeProvider> registryBuilder = getAuthSchemeProviderRegistryBuilder(connectivityDetails.getAuthScheme());
        //  RequestConfig requestConfig = requestConfigBuilder();

        httpClientContext.setAuthSchemeRegistry(registryBuilder);
        httpClientContext.setCredentialsProvider(credentialsProvider);
        //   httpClientContext.setRequestConfig(requestConfig);
        return httpClientContext;
    }

    private final Registry<AuthSchemeProvider> getAuthSchemeProviderRegistryBuilder(final String authScheme) {
        RegistryBuilder<AuthSchemeProvider> registryBuilder = RegistryBuilder.create();
        registryBuilder.register(authScheme, AuthSchemeFactory.getAuthSchemeProvider(authScheme));
        return registryBuilder.build();
    }

    private final CredentialsProvider getCredentialsProvider(ConnectivityDetails connectivityDetails) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(connectivityDetails.getUsername(), connectivityDetails.getPassword()));
        return credentialsProvider;
    }

 /*   private RequestConfig requestConfigBuilder() {
        return RequestConfig.custom().
                setConnectionRequestTimeout(connectionTimeout).
                setConnectTimeout(connectionTimeout).
                setSocketTimeout(socketTimeout).
                build();
    }*/
}
