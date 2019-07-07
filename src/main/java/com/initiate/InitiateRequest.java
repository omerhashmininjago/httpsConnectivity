package com.initiate;

import com.details.spec.ConnectivityDetails;
import com.https.connectivity.ConnectionUtil;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class InitiateRequest {

    @Autowired
    private ConnectionUtil connectionUtil;

    @Autowired
    private CloseableHttpClient httpClient;

    public void retrieveData(final ConnectivityDetails connectivityDetails) {

        HttpGet httpGetRequest = new HttpGet(connectivityDetails.getEndPointUrl());
        HttpClientContext httpClientContext = connectionUtil.getContext(connectivityDetails);

        // CloseableHttpResponse implements Closeable, so that connection is closed for good once done with the task
        try (CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpGetRequest, httpClientContext)) {

            if (closeableHttpResponse != null) {
                if (HttpStatus.SC_OK != closeableHttpResponse.getStatusLine().getStatusCode()) {
                    //something went wrong while retrieving data
                    return;
                } else if (closeableHttpResponse.getEntity().getContent() == null) {
                    // content not received
                    return;
                }

                // Always make sure the input stream is closed once the task is performed
                try (InputStream inputStream = closeableHttpResponse.getEntity().getContent();) {

                } catch (IOException e) {

                }
            }
        } catch (IOException e) {

        } finally {
            if (httpGetRequest != null) {
                httpGetRequest.releaseConnection();
            }
        }
    }
}
