package PetCryptoViewer.Client;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

@Component
public class HTTPClient {
    @Autowired
    private CloseableHttpClient httpClient;

    public String getHttpRequest(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        HttpEntity httpEntity;
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        httpEntity = httpResponse.getEntity();

        return EntityUtils.toString(httpEntity);
    }
}
