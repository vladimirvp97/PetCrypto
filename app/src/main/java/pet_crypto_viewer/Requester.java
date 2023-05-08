package pet_crypto.Request;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pet_crypto_viewer.Service;

@EnableScheduling
@Component
public class Requester {
    @Autowired
    Service service;
    @Scheduled(fixedDelay = 3000)
    public void scheduledRequestPriceBTC() throws Exception {
        String externalServerUrl = "https://api.blockchain.com/v3/exchange/tickers/BTC-USD";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(externalServerUrl);
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        String response = EntityUtils.toString(httpEntity);

        httpResponse.close();
        httpClient.close();

        service.extractAndPushPriceBTC(response);
    }
}
